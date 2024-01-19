package com.ccor.ecommerce.model.dto;

public record AddressEditRequestDTO(
        String street,
        String country,
        String postalCode
) {
}
