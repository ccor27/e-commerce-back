package com.ccor.ecommerce.model.dto;

import java.util.List;

public record CustomerRequestEditDTO(
        String name,
        String lastName,
        String cellphone,
        String email,
        String username,
        List<String> channels,
        boolean receiveNotifications
) {
}
