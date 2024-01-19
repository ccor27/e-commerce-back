package com.ccor.ecommerce.model.dto;

public record ProductStockResponseDTO(
        Long id,
        String name,
        int amount,
        int pricePerUnit,
        String barCode,
        boolean enableProduct,
        String picturePath
) {
}
