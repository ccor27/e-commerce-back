package com.ccor.ecommerce.model.dto;

import com.ccor.ecommerce.model.ProductSold;

import java.util.Date;
import java.util.List;

public record SaleResponseDTO(
        Long id,
        String concept,
        List<ProductSoldResponseDTO> products,
        Date createAt
) {
}
