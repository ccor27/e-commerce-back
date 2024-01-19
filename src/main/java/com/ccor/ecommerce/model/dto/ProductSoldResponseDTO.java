package com.ccor.ecommerce.model.dto;

public record ProductSoldResponseDTO(
        Long id,
        String barCode,
        String name,
        int amount,
        int price
) {
}
