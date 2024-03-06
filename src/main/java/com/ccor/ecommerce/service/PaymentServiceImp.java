package com.ccor.ecommerce.service;

import com.ccor.ecommerce.exceptions.PaymentException;
import com.ccor.ecommerce.exceptions.SaleException;
import com.ccor.ecommerce.model.*;
import com.ccor.ecommerce.model.dto.*;
import com.ccor.ecommerce.repository.CreditCardRepository;
import com.ccor.ecommerce.repository.CustomerRepository;
import com.ccor.ecommerce.repository.PaymentRepository;
import com.ccor.ecommerce.service.mapper.CreditCardDTOMapper;
import com.ccor.ecommerce.service.mapper.PaymentDTOMapper;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCancelParams;
import com.stripe.param.PaymentIntentConfirmParams;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImp implements IPaymentService{

    private PaymentRepository paymentRepository;
    private CreditCardRepository creditCardRepository;
    private CustomerRepository customerRepository;
    private PaymentDTOMapper paymentDTOMapper;
    private CreditCardDTOMapper creditCardDTOMapper;
    private ISaleService iSaleService;
    private INotificationService iNotificationService;
    @Autowired
    public PaymentServiceImp(PaymentRepository paymentRepository, CreditCardRepository creditCardRepository,
                             CustomerRepository customerRepository, PaymentDTOMapper paymentDTOMapper,
                             CreditCardDTOMapper creditCardDTOMapper, ISaleService iSaleService,
                             INotificationService iNotificationService) {
        this.paymentRepository = paymentRepository;
        this.creditCardRepository = creditCardRepository;
        this.customerRepository = customerRepository;
        this.paymentDTOMapper = paymentDTOMapper;
        this.creditCardDTOMapper = creditCardDTOMapper;
        this.iSaleService = iSaleService;
        this.iNotificationService = iNotificationService;
    }

    @Override
    public PaymentResponseDTO save(PaymentRequestDTO paymentRequestDTO) {
        if(paymentRequestDTO!=null){
            Payment payment = Payment.builder()
                    .createAt(new Date())
                    .card(findCreditCard(paymentRequestDTO.card().id()))
                    .build();
              payment = paymentRepository.save(payment);
              return paymentDTOMapper.apply(payment);
        }else{
            throw new PaymentException("The request to save is null");
        }
    }

    @Override
    public PaymentResponseDTO confirm(Long id,Long customerId) {
        Payment p = paymentRepository.findById(id).orElse(null);
        if(p!=null){
            try{
                PaymentIntent paymentIntent = PaymentIntent.retrieve(p.getIdStripePayment());
                PaymentIntentConfirmParams params =
                        PaymentIntentConfirmParams.builder()
                                .setPaymentMethod("pm_card_visa")
                                .setReturnUrl("https://www.example.com")
                                .build();
                p.setStatusPayment(StatusPayment.PAID);
                p.setIdStripePayment(paymentIntent.getId());
                paymentIntent.confirm(params).toJson();
                paymentRepository.save(p);
                //notify
                notifyConfirmation(customerId,paymentIntent.getId());
                return paymentDTOMapper.apply(p);
            } catch (StripeException e) {
                throw new SaleException("Error during the payment's confirmation : "+e.getLocalizedMessage());
            }
        }else{
            throw new SaleException("Error the payment doesn't exist to confirm it!");
        }
    }

    @Override
    @Transactional
    public PaymentResponseDTO cancel(Long id,Long customerId) {
        Payment p = paymentRepository.findById(id).orElse(null);
        if(p!=null){
            try {
                PaymentIntent resource = PaymentIntent.retrieve(p.getIdStripePayment());
                PaymentIntentCancelParams params = PaymentIntentCancelParams.builder()
                        .setCancellationReason(PaymentIntentCancelParams.CancellationReason.REQUESTED_BY_CUSTOMER)
                        .build();
                PaymentIntent paymentIntent = resource.cancel(params);
                p.setIdStripePayment(paymentIntent.getId());
                p.setStatusPayment(StatusPayment.CANCEL);
                p.setDeleted(true);
                iSaleService.cancelSale(p.getId());
                //notify
                notifyCancel(customerId,paymentIntent.getId());
                return paymentDTOMapper.apply(paymentRepository.save(p));
            } catch (StripeException e) {
                throw new SaleException("Error during the payment's cancellation : "+e.getLocalizedMessage());
            }
        }else{
            throw new SaleException("Error the payment doesn't exist to cancel it!");
        }
    }

    private void notifyConfirmation(Long customerId, String paymentIntent){
     Customer c = customerRepository.findById(customerId).orElse(null);
     if(c!=null){
         if(c.getChannelNotifications().contains(ChannelNotification.EMAIL)){
             String message = buildEmailConfirmationNotification(paymentIntent);
             iNotificationService.notifyByEmailConfirmedPayment(customerId,message);
         }
         if(c.getChannelNotifications().contains(ChannelNotification.SMS)){
             iNotificationService.notifyBySmsConfirmedPayment(customerId,buildSmsConfirmationNotification(paymentIntent));
         }
     }
    }
    private void notifyCancel(Long customerId, String paymentIntent){
        Customer c = customerRepository.findById(customerId).orElse(null);
        if(c!=null){
            if(c.getChannelNotifications().contains(ChannelNotification.EMAIL)){
                String message = buildEmailCancelNotification(paymentIntent);
                iNotificationService.notifyByEmailPaymentCancelled(customerId,message);
            }
            if(c.getChannelNotifications().contains(ChannelNotification.SMS)){
                iNotificationService.notifyBySmsPaymentCancelled(customerId,buildSmsCancelNotification(paymentIntent));
            }
        }
    }
    private StatusPayment knowStatus(String status){
        return switch (status.toLowerCase()) {
            case "paid" -> StatusPayment.PAID;
            case "unpaid" -> StatusPayment.UNPAID;
            default -> null;
        };
    }
    private CreditCard findCreditCard(Long id){
       CreditCard c = creditCardRepository.findById(id).orElse(null);
       return c;
    }
    @Override
    public List<PaymentResponseDTO> findAll(Integer offset, Integer pageSize) {
        int totalPayments = paymentRepository.countAllPayments();
        int adjustedOffset = pageSize*offset;
        adjustedOffset=Math.min(adjustedOffset,totalPayments);
        if(adjustedOffset>=totalPayments){
            throw new PaymentException("The aren't enough payments");
        }else{
            Page<Payment> list = paymentRepository.findAll(PageRequest.of(offset,pageSize));
            if(list!=null){
                return list.stream().map(payment -> {
                    return paymentDTOMapper.apply(payment);
                }).collect(Collectors.toList());
            }else{
                throw new PaymentException("The list of payments is null");
            }
        }
    }

    @Override
    public List<Payment> findAllToExport(Integer offset, Integer pageSize) {
        return paymentRepository.findAll(PageRequest.of(offset,pageSize)).getContent();
    }

    @Override
    public List<Payment> findAllToExport() {
        return paymentRepository.findAll();
    }

    @Override
    public PaymentResponseDTO findById(Long id) {
        Payment payment = paymentRepository.findById(id).orElse(null);
        if(payment!=null){
            return paymentDTOMapper.apply(payment);
        }else{
            throw new PaymentException("The payment fetched by id doesn't exist");
        }
    }

    @Override
    public boolean remove(Long id) {
        Payment payment = paymentRepository.findById(id).orElse(null);
        if(payment!=null){
            payment.setStatusPayment(StatusPayment.CANCEL);
            payment.setDeleted(true);
            paymentRepository.save(payment);
          return true;
        }else{
            throw new PaymentException("The payment fetched to delete doesn't exist");
        }
    }

    @Override
    public List<PaymentResponseDTO> findPaymentsByStatusAndEnable(Integer offset, Integer pageSize, String statusPayment) {
        StatusPayment status = knowStatus(statusPayment);
        int totalPayments = paymentRepository.countPaymentsByStatusPaymentAndEnable(status);
        int adjustedOffset = pageSize*offset;
        adjustedOffset=Math.min(adjustedOffset,totalPayments);
        if(adjustedOffset>=totalPayments){
            throw new PaymentException("The aren't enough payments");
        }else{
            Page<Payment> list = paymentRepository.findPaymentsByStatusAndEnable(status,PageRequest.of(offset,pageSize));
            if(list!=null){
                return list.stream().map(payment -> {
                    return paymentDTOMapper.apply(payment);
                }).collect(Collectors.toList());
            }else{
                throw new PaymentException("The list of payments is null");
            }
        }
    }

    @Override
    public List<PaymentResponseDTO> findAllPaymentsByStatus(Integer offset, Integer pageSize, String statusPayment) {
            StatusPayment status = knowStatus(statusPayment);
        int totalPayments = paymentRepository.countAllPaymentsByStatusPayment(status);
        int adjustedOffset = pageSize*offset;
        adjustedOffset=Math.min(adjustedOffset,totalPayments);
        if(adjustedOffset>=totalPayments){
            throw new PaymentException("The aren't enough payments");
        }else{
            Page<Payment> list = paymentRepository.findPaymentsByStatus(status,PageRequest.of(offset,pageSize));
            if(list!=null){
                return list.stream().map(payment -> {
                    return paymentDTOMapper.apply(payment);
                }).collect(Collectors.toList());
            }else{
                throw new PaymentException("The list of payments is null");
            }
        }
    }

    @Override
    public CreditCardResponseDTO findCardPayment(Long id) {
        CreditCard card = paymentRepository.findCard(id).orElse(null);
        if(card!=null){
            return creditCardDTOMapper.apply(card);
        }else{
            throw new PaymentException("The payment's card doesn't exist");
        }
    }

    private String buildSmsConfirmationNotification(String paymentIntent){
        return "Congrats your sale:"+paymentIntent+"\n" +
                "Was paid successfully.\n Ecommerce team.";
    }
    private String buildSmsCancelNotification(String paymentIntent){
        return "You sale:"+paymentIntent+"\n" +
                "Was paid successfully.\n Ecommerce team.";
    }
    private String buildEmailConfirmationNotification(String paymentIntent){
        return "<div style=\"font-family:Helvetica, Arial, sans-serif; font-size:16px; margin:0; color:#0b0c0c\">\n" +
                "    <p style=\"Margin:0 0 20px 0; font-size:19px; line-height:25px; color:#0b0c0c\">Congrats your sale: "+paymentIntent+" </p>\n" +
                "    <p style=\"Margin:0 0 20px 0; font-size:19px; line-height:25px; color:#0b0c0c\">Was paid successfully, thank you so much for your sale and we hope you enjoy it!</p>\n" +
                "</div>";
    }
    private String buildEmailCancelNotification(String paymentIntent){
        return "<div style=\"font-family:Helvetica, Arial, sans-serif; font-size:16px; margin:0; color:#0b0c0c\">\n" +
                "    <p style=\"Margin:0 0 20px 0; font-size:19px; line-height:25px; color:#0b0c0c\">You cancelled your sale: "+paymentIntent+" </p>\n" +
                "    <p style=\"Margin:0 0 20px 0; font-size:19px; line-height:25px; color:#0b0c0c\">Your paid was cancelled successfully, we hope you come back to buy, see you soon!</p>\n" +
                "</div>";
    }
}
