package com.ccor.ecommerce.model.dto;

public record ProductSoldRequestDTO(
        String barCode,
        String name,
        int amount,
        double price
) {
}
