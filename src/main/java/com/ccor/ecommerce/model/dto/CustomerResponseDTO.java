package com.ccor.ecommerce.model.dto;

public record CustomerResponseDTO(
        Long id,
        String name,
        String lastName,
        String cellphone,
        String email,
        String username
) {
}
