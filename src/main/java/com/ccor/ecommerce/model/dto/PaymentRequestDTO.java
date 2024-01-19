package com.ccor.ecommerce.model.dto;

import java.util.Date;

public record PaymentRequestDTO(
        Date createAt,
        CreditCardResponseDTO card
) {
}
