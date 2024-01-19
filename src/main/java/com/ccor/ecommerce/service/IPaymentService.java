package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.Payment;
import com.ccor.ecommerce.model.ProductSold;
import com.ccor.ecommerce.model.dto.*;

import java.util.List;

public interface IPaymentService {
    PaymentResponseDTO save (PaymentRequestDTO paymentRequestDTO);
    PaymentResponseDTO confirm(Long id,Long customerId);
    PaymentResponseDTO cancel(Long id,Long customerId);
    List<PaymentResponseDTO> findAll(Integer offset,Integer pageSize);
    List<Payment> findAllToExport(Integer offset, Integer pageSize);
    List<Payment> findAllToExport();
    PaymentResponseDTO findById(Long id);
    boolean remove(Long id);
    List<PaymentResponseDTO> findPaymentsByStatusAndEnable(Integer offset, Integer pageSize, String statusPayment);
    List<PaymentResponseDTO> findAllPaymentsByStatus(Integer offset, Integer pageSize, String statusPayment);
    CreditCardResponseDTO findCardPayment(Long id);

}
