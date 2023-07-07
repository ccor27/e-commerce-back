package com.ccor.ecommerce.model.dto;

public record ProductStockRequestDTO(
        String name,
        int amount,
        double pricePerUnit,
        String barCode,
        boolean enableProduct
) {
}
