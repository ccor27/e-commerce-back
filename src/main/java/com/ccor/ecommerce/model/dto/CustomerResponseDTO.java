package com.ccor.ecommerce.model.dto;

import java.util.List;

public record CustomerResponseDTO(
        Long id,
        String name,
        String lastName,
        String cellphone,
        String email,
        String username,
        List<String> channels,
        String picturePath
) {
}
