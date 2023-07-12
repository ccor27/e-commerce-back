package com.ccor.ecommerce.service.mapper;

import com.ccor.ecommerce.model.ProductStock;
import com.ccor.ecommerce.model.dto.ProductStockResponseDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class ProductStockDTOMapper implements Function<ProductStock, ProductStockResponseDTO> {
    @Override
    public ProductStockResponseDTO apply(ProductStock productStock) {
        return new ProductStockResponseDTO(
                productStock.getId(),
                productStock.getName(),
                productStock.getAmount(),
                productStock.getPricePerUnit(),
                productStock.getBarCode(),
                productStock.isEnableProduct()
        );
    }
}
