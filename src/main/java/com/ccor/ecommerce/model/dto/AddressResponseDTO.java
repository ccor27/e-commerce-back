package com.ccor.ecommerce.model.dto;

public record AddressResponseDTO(
        Long id,
        String street,
        String country,
        String postalCode
) {
}
