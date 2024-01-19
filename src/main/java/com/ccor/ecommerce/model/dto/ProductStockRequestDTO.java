package com.ccor.ecommerce.model.dto;


import org.springframework.web.multipart.MultipartFile;

public record ProductStockRequestDTO(
        String name,
        int amount,
        int pricePerUnit,
        String barCode,
        boolean enableProduct
) {}

