package com.ccor.ecommerce.model.dto;

import java.util.Date;

public record PaymentResponseDTO(
        Long id,
        String statusPayment,
        Date createAt,
        Long customerId,
        Long creditCardId,
        Long saleId

) {
}
