package com.ccor.ecommerce.model.dto;

public record AuthenticationRequestDTO(
        String username,
        String password
) {
}
