package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.ProductStock;
import com.ccor.ecommerce.model.dto.ProductStockRequestDTO;
import com.ccor.ecommerce.model.dto.ProductStockResponseDTO;
import com.ccor.ecommerce.repository.ProductStockRepository;

import java.util.List;

public interface IProductStockService {
    ProductStockResponseDTO save(ProductStockRequestDTO productStockRequestDTO);
    boolean remove(Long id);
    ProductStockResponseDTO edit(ProductStockRequestDTO productStockRequestDTO, Long id);
    List<ProductStockResponseDTO> findProductStocksByEnableProduct();
    ProductStockResponseDTO findProductStocksByBarCode(String barCode);
    ProductStockResponseDTO sellProduct(int amountSold, Long id);
}
