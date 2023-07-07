package com.ccor.ecommerce.model.dto;

public record ProductStockResponseDTO(
        Long id,
        String name,
        int amount,
        double pricePerUnit,
        String barCode,
        boolean enableProduct
) {
}
