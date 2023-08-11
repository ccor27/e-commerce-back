package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.dto.*;

import java.util.List;

public interface IPaymentService {
    PaymentResponseDTO save (PaymentRequestDTO paymentRequestDTO);
    PaymentResponseDTO edit(PaymentRequestDTO paymentRequestDTO, Long id);
    PaymentResponseDTO changeStatus(String status,Long id);
    List<PaymentResponseDTO> findAll(Integer offset,Integer pageSize);
    PaymentResponseDTO findById(Long id);
    boolean remove(Long id);
    List<PaymentResponseDTO> findPaymentsByStatus(Integer offset, Integer pageSize, String statusPayment);
    CustomerResponseDTO findCustomerPayment(Long id);
    CreditCardResponseDTO findCardPayment(Long id);

}
