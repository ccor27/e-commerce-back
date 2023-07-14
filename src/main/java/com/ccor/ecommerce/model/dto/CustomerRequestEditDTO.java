package com.ccor.ecommerce.model.dto;

public record CustomerRequestEditDTO(
        String name,
        String lastName,
        String cellphone,
        String email,
        String username
) {
}
