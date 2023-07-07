package com.ccor.ecommerce.model.dto;

public record CreditCardResponseDTO(
        Long id,
        String number,
        String type
) {
}
