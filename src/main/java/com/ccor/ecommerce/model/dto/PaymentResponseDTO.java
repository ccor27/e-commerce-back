package com.ccor.ecommerce.model.dto;

import java.util.Date;

public record PaymentResponseDTO(
        Long id,
        String paymentIntent,
        String statusPayment,
        Date createAt,
        Long creditCardId,
        int totalPrice

) {
}
