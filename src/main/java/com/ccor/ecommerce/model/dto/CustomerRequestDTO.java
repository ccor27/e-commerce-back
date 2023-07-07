package com.ccor.ecommerce.model.dto;

public record CustomerRequestDTO(
        String name,
        String lastName,
        String cellphone,
        String email,
        String username,
        String pwd,
        AddressRequestDTO address
) {
}
