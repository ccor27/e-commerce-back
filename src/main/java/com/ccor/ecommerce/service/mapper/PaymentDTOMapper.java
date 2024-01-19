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
                payment.getIdStripePayment(),
                payment.getStatusPayment().name(),
                payment.getCreateAt(),
                existCard(payment),
                payment.getTotalPrice()
        );
    }
    private Long existCard(Payment payment){
        CreditCard card = payment.getCard();
        return card!=null ? card.getId() : null;
    }
}
