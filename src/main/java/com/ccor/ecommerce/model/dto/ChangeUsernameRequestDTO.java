package com.ccor.ecommerce.model.dto;

public record ChangeUsernameRequestDTO(
        Long id,
        String currentUsername,
        String newUsername
) {
}
