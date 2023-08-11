package com.ccor.ecommerce.service;

import com.ccor.ecommerce.exceptions.PaymentException;
import com.ccor.ecommerce.model.*;
import com.ccor.ecommerce.model.dto.*;
import com.ccor.ecommerce.repository.CreditCardRepository;
import com.ccor.ecommerce.repository.CustomerRepository;
import com.ccor.ecommerce.repository.PaymentRepository;
import com.ccor.ecommerce.repository.SaleRepository;
import com.ccor.ecommerce.service.mapper.CreditCardDTOMapper;
import com.ccor.ecommerce.service.mapper.CustomerDTOMapper;
import com.ccor.ecommerce.service.mapper.PaymentDTOMapper;
import com.ccor.ecommerce.service.mapper.SaleDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImp implements IPaymentService{
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private PaymentDTOMapper paymentDTOMapper;
    @Autowired
    private CustomerDTOMapper customerDTOMapper;
    @Autowired
    private CreditCardDTOMapper creditCardDTOMapper;

    @Override
    public PaymentResponseDTO save(PaymentRequestDTO paymentRequestDTO) {
        if(paymentRequestDTO!=null){
            Payment payment = Payment.builder()
                    .statusPayment(knowStatus(paymentRequestDTO.statusPayment()))
                    .createAt(new Date())
                    .customer(findCustomer(paymentRequestDTO.customer().id()))
                    .card(findCreditCard(paymentRequestDTO.card().id()))
                   // .sale(findSale(paymentRequestDTO.sale().id()))
                    .build();
              payment = paymentRepository.save(payment);
              return paymentDTOMapper.apply(payment);
        }else{
            throw new PaymentException("The request to save is null");
        }
    }
    private StatusPayment knowStatus(String status){
        switch (status.toLowerCase()){
            case "paid":
                return StatusPayment.PAID;
            case "unpaid":
                return StatusPayment.UNPAID;
            default:
                return null;
        }
    }
    private Customer findCustomer(Long id){
        Customer c = customerRepository.findById(id).orElse(null);
        return c;
    }
    private CreditCard findCreditCard(Long id){
       CreditCard c = creditCardRepository.findById(id).orElse(null);
       return c;
    }
    @Override
    public PaymentResponseDTO edit(PaymentRequestDTO paymentRequestDTO, Long id) {
        Payment payment = paymentRepository.findById(id).orElse(null);
        if(payment!=null && paymentRequestDTO!=null){
            payment.setStatusPayment(knowStatus(paymentRequestDTO.statusPayment()));
            payment.setCustomer(findCustomer(paymentRequestDTO.customer().id()));
            payment.setCard(findCreditCard(paymentRequestDTO.card().id()));
            payment.setCreateAt(paymentRequestDTO.createAt());
            payment = paymentRepository.save(payment);
            return paymentDTOMapper.apply(payment);
        }else{
            throw new PaymentException("The payment fetched to update doesn't exist or the request is null");
        }
    }

    @Override
    public PaymentResponseDTO changeStatus(String status, Long id) {
        Payment payment = paymentRepository.findById(id).orElse(null);
        if(payment!=null){
            payment.setStatusPayment(knowStatus(status));
            payment = paymentRepository.save(payment);
            return paymentDTOMapper.apply(payment);
        }else{
            throw new PaymentException("The payment fetched to change its status doesn't exist");
        }
    }

    @Override
    public List<PaymentResponseDTO> findAll(Integer offset, Integer pageSize) {
        Page<Payment> list = paymentRepository.findAll(PageRequest.of(offset,pageSize));
        if(list!=null){
            return list.stream().map(payment -> {
                return paymentDTOMapper.apply(payment);
            }).collect(Collectors.toList());
        }else{
            throw new PaymentException("The list of payments is null");
        }
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
          paymentRepository.delete(payment);
          return true;
        }else{
            throw new PaymentException("The payment fetched to delete doesn't exist");
        }
    }

    @Override
    public List<PaymentResponseDTO> findPaymentsByStatus(Integer offset, Integer pageSize, String statusPayment) {
        Page<Payment> list = paymentRepository.findPaymentsByStatus(knowStatus(statusPayment),PageRequest.of(offset,pageSize));
        if(list!=null){
            return list.stream().map(payment -> {
                return paymentDTOMapper.apply(payment);
            }).collect(Collectors.toList());
        }else{
            throw new PaymentException("The list of payments is null");
        }
    }

    @Override
    public CustomerResponseDTO findCustomerPayment(Long id) {
        Customer customer = paymentRepository.findCustomer(id).orElse(null);
        if(customer!=null){
            return customerDTOMapper.apply(customer);
        }else{
            throw new PaymentException("The payment's customer doesn't exist");
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
}
