package com.ccor.ecommerce.model.dto;

import com.ccor.ecommerce.model.ProductSold;

import java.util.Date;
import java.util.List;

public record SaleRequestDTO(
        String concept,
        List<ProductSold> products,
        Date createAt
) {
}
