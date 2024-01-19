package com.ccor.ecommerce.model.dto;

public record ChangePwdRequestDTO(
        Long id,
        String currentPassword,
        String newPassword
) {
}
