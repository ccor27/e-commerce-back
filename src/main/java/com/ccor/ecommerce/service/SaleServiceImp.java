package com.ccor.ecommerce.service;

import com.ccor.ecommerce.exceptions.SaleException;
import com.ccor.ecommerce.model.*;
import com.ccor.ecommerce.model.dto.*;
import com.ccor.ecommerce.repository.*;
import com.ccor.ecommerce.service.mapper.PaymentDTOMapper;
import com.ccor.ecommerce.service.mapper.ProductSoldDTOMapper;
import com.ccor.ecommerce.service.mapper.SaleDTOMapper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;

import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SaleServiceImp implements ISaleService{
    @Value("${stripe.test.secret.key}")
    private String stripeSecretKey;
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ICanceledSaleService iCanceledSaleService;
    @Autowired
    private IProductStockService iProductStockService;
    @Autowired
    private INotificationService iNotificationService;
    @Autowired
    private SaleDTOMapper saleDTOMapper;
    @Autowired
    private PaymentDTOMapper paymentDTOMapper;
    @Autowired
    private ProductSoldDTOMapper productSoldDTOMapper;
    @Override
    @Transactional
    public SaleResponseDTO save(SaleRequestDTO saleRequestDTO,Long customerId) {
        Customer c = customerRepository.findById(customerId).orElse(null);
        if(saleRequestDTO!=null && c!=null){
            History h = c.getHistory();
            Sale sale;
            int totalPrice = 0;
            if(saleRequestDTO.products()!=null){
                List<ProductSold> productSold = new ArrayList<>();
                for (ProductSoldRequestDTO productSoldRequestDTO:saleRequestDTO.products() ) {
                     iProductStockService.sellProduct(productSoldRequestDTO.amount(), productSoldRequestDTO.barCode());
                    productSold.add(new ProductSold(null, productSoldRequestDTO.barCode(), productSoldRequestDTO.name(),
                                         productSoldRequestDTO.amount(), productSoldRequestDTO.price()));
                    totalPrice+=productSoldRequestDTO.price();
                }
                sale=Sale.builder()
                        .concept(saleRequestDTO.concept())
                        .productsSold(productSold)
                        .createAt(new Date())
                        .build();
            }else{
                sale=Sale.builder()
                        .concept(saleRequestDTO.concept())
                        .productsSold(new ArrayList<>())
                        .createAt(new Date())
                        .build();
            }
            if(saleRequestDTO.payment()==null){
                throw new SaleException("The payment data is required, it can't be null");
            }

            PaymentRequestDTO paymentRequestDTO = saleRequestDTO.payment();
            if(paymentRequestDTO==null || paymentRequestDTO.card()==null){
                throw new SaleException("The payment request and the card is required");
            }
            CreditCard card = creditCardRepository.findCreditCardByNumber(paymentRequestDTO.card().number())
                    .orElse(null);
            if(card==null){
                throw new SaleException("The card to pay doesn't exist");
            }
            try {
                //make the call to stripe
                //create paymentIntent
                Stripe.apiKey= stripeSecretKey;
                   PaymentIntentCreateParams params =
                        PaymentIntentCreateParams.builder()
                                .setAmount((long) totalPrice*100)
                                .setCurrency("usd")
                                .setDescription(sale.getConcept())
                                .setAutomaticPaymentMethods(
                                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                                .setEnabled(true)
                                                .build()
                                )
                                .build();
                PaymentIntent paymentIntent = PaymentIntent.create(params);
                sale.setPayment(
                        new Payment(
                                null,
                                paymentIntent.getId(),
                                StatusPayment.UNPAID,
                                saleRequestDTO.payment().createAt(),
                                false,
                                card,
                                totalPrice));
                //the payment was created successfully but need be confirmed
                sale = saleRepository.save(sale);
                h.getSales().add(sale);
                customerRepository.saveAndFlush(c);
                saleCreatedNotification(c);
                return saleDTOMapper.apply(sale);

            } catch (StripeException e) {
                throw new SaleException("Error during the payment: "+e.getLocalizedMessage());
            }

        }else{
         throw new SaleException("The request to save is null or the history doesn't exist");
        }
    }
    private void saleCreatedNotification(Customer c){
        if(c!=null){
            String message = "Thank you for your recent purchase from our ecommerce!.\n " +
                    "Please remember confirm your payment.";
            if(c.getChannelNotifications().contains(ChannelNotification.EMAIL)){
                iNotificationService.notifyByEmailNewSale(c.getId(),message);
            }
            if(c.getChannelNotifications().contains(ChannelNotification.SMS)){
                iNotificationService.notifyBySmsNewSale(c.getId(),message);
            }
        }else{
            throw new SaleException("Error during the process of notifying, the customer doesn't exist");
        }
    }
    //TODO: crete a method to refund
    @Override
    public SaleResponseDTO findById(Long id) {
        if(saleRepository.existsById(id)){
            return saleDTOMapper.apply(saleRepository.findById(id).get());
        }else{
            throw new SaleException("The sale fetched by id doesn't exist");
        }
    }

    @Override
    public List<SaleResponseDTO> findAll(Integer offset, Integer pageSize) {
        int totalSales = saleRepository.countSales();
        int adjustedOffset = pageSize*offset;
        adjustedOffset = Math.min(adjustedOffset,totalSales);
        if(adjustedOffset>=totalSales){
            throw new SaleException("There aren't enough sales");
        }else{
            Page<Sale> list = saleRepository.findAll(PageRequest.of(offset,pageSize));
            if(list!=null){
                return list.stream().map(sale -> {
                    return saleDTOMapper.apply(sale);
                }).collect(Collectors.toList());
            }else{
                throw new SaleException("The list of sales is null");
            }
        }

    }

    @Override
    public List<Sale> findAllToExport(Integer offset, Integer pageSize) {
        return saleRepository.findAll(PageRequest.of(offset,pageSize)).getContent();
    }

    @Override
    public List<Sale> findAllToExport() {
        return saleRepository.findAll();
    }

    @Override
    public List<SaleResponseDTO> findSalesByDate(Date createAt,Integer offset,Integer pageSize) {
        int totalSales = saleRepository.countSalesByDate(createAt);
        int adjustedOffset = pageSize*offset;
        adjustedOffset = Math.min(adjustedOffset,totalSales);
        if(adjustedOffset>=totalSales) {
            throw new SaleException("The aren't enough sales");
        }else {
            List<Sale> sales = saleRepository.findSalesByDate(createAt,PageRequest.of(offset,pageSize));
            if(!sales.isEmpty() && sales!=null){
                return sales.stream().map(sale -> {
                    return saleDTOMapper.apply(sale);
                }).collect(Collectors.toList());
            }else{
                throw new SaleException("The aren't sales in that date");
            }
        }

    }

    @Override
    public List<SaleResponseDTO> findSalesBetweenDate(Date createAtOne, Date createAtTwo,Integer offset,Integer pageSize) {
        int totalSales = saleRepository.countSalesByDateRange(createAtOne,createAtTwo);
        int adjustedOffset = pageSize*offset;
        adjustedOffset = Math.min(adjustedOffset,totalSales);
        if(adjustedOffset>=totalSales) {
            throw new SaleException("The aren't enough sales");
        }else {
            List<Sale> sales = saleRepository.findSalesByDateRange(createAtOne,createAtTwo,PageRequest.of(offset,pageSize));
            if(!sales.isEmpty() && sales!=null){
                return sales.stream().map(sale -> {
                    return saleDTOMapper.apply(sale);
                }).collect(Collectors.toList());
            }else{
                throw new SaleException("The aren't sales in that date");
            }
        }
    }

    @Override
    public List<ProductSoldResponseDTO> findProductsSold(Long id, Integer offset,Integer pageSize) {
        if(saleRepository.existsById(id)){
            int totalAddresses = saleRepository.countProductsSold(id);
            int adjustedOffset = pageSize*offset;
            adjustedOffset=Math.min(adjustedOffset,totalAddresses);
            if(adjustedOffset>=totalAddresses){
                throw new SaleException("There aren't enough products sold in the sale");
            }else{
                Page<ProductSold> list = saleRepository.findSaleProductsSold(id,PageRequest.of(offset,pageSize));
                if(list!=null){
                    return list.stream().map(productSold -> {
                        return productSoldDTOMapper.apply(productSold);
                    }).collect(Collectors.toList());
                }else{
                    throw new SaleException("The list of sale's product is null");
                }
            }
        }else{
            throw new SaleException("The sale fetched to find its product doesn't exist");
        }
    }

    @Override
    public PaymentResponseDTO findPayment(Long id) {
        Payment payment = saleRepository.findPaymentSale(id).orElse(null);
        if(payment!=null){
            return paymentDTOMapper.apply(payment);
        }else{
            throw new SaleException("The sale fetch doesn't exist or the sale has not payment yet");
        }

    }

    @Override
    public void cancelSale(Long paymentId) {
     Sale s = saleRepository.findSaleByPayment(paymentId);
     if(s!=null){
         List<ProductSold> productsSold = s.getProductsSold();
         if(productsSold!=null && !productsSold.isEmpty()){
             for (ProductSold p:productsSold) {
                 //put back the amount sold to the products
                 iProductStockService.addBackAmount(p.getBarCode(),p.getAmount());
             }
         }
         s.setDeleted(true);
         saleRepository.save(s);
         //save the canceled sale
         iCanceledSaleService.save(new CanceledSale (null,s.getId(),paymentId,new Date()));
     }else{
         throw new SaleException("The sale to cancel doesn't exist");
     }
    }
}
