package com.ccor.ecommerce.service.mapper;

import com.ccor.ecommerce.model.ProductSold;
import com.ccor.ecommerce.model.dto.ProductSoldResponseDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class ProductSoldDTOMapper implements Function<ProductSold, ProductSoldResponseDTO> {
    @Override
    public ProductSoldResponseDTO apply(ProductSold productSold) {
        return new ProductSoldResponseDTO(
                productSold.getId(),
                productSold.getBarCode(),
                productSold.getName(),
                productSold.getAmount(),
                productSold.getPrice()
        );
    }
}
