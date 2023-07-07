package com.ccor.ecommerce.model.dto;

public record AddressRequestDTO(
        String street,
        String country,
        String postalCode
) {
}
