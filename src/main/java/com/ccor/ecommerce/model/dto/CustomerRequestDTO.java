package com.ccor.ecommerce.model.dto;

import java.util.List;

public record CustomerRequestDTO(
        String name,
        String lastName,
        String cellphone,
        String email,
        String username,
        String pwd,
        List<String> channels
) {
}
