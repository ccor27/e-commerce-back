package com.ccor.ecommerce.model.dto;

import java.util.Date;

public record PaymentRequestDTO(
        String statusPayment,
        Date createAt,
        CustomerResponseDTO customer,
        CreditCardResponseDTO card
) {
}
