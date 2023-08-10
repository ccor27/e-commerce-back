package com.ccor.ecommerce.service.mapper;

import com.ccor.ecommerce.model.CreditCard;
import com.ccor.ecommerce.model.Customer;
import com.ccor.ecommerce.model.Payment;
import com.ccor.ecommerce.model.Sale;
import com.ccor.ecommerce.model.dto.PaymentResponseDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class PaymentDTOMapper implements Function<Payment, PaymentResponseDTO> {
    @Override
    public PaymentResponseDTO apply(Payment payment) {
        return new PaymentResponseDTO(
                payment.getId(),
                payment.getStatusPayment().name(),
                payment.getCreateAt(),
                existCustomer(payment),
                existCard(payment),
                existSale(payment)
        );
    }
    private Long existCustomer(Payment payment){
        Customer customer = payment.getCustomer();
        return customer!=null ? customer.getId() : null;
    }
    private Long existCard(Payment payment){
        CreditCard card = payment.getCard();
        return card!=null ? card.getId() : null;
    }
    private Long existSale(Payment payment){
        Sale sale = payment.getSale();
        return sale!=null ? sale.getId() : null;
    }
}
