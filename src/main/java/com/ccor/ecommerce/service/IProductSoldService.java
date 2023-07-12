package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.ProductSold;
import com.ccor.ecommerce.model.dto.ProductSoldRequestDTO;
import com.ccor.ecommerce.model.dto.ProductSoldResponseDTO;

import java.util.List;

public interface IProductSoldService {
    ProductSoldResponseDTO save(ProductSoldRequestDTO productSoldRequestDTO);
    ProductSoldResponseDTO edit(ProductSoldRequestDTO productSoldRequestDTO, Long id);
    boolean remove(Long id);
    ProductSoldResponseDTO findById(Long id);
    List<ProductSoldResponseDTO> findAll();
    List<ProductSoldResponseDTO> findProductsSoldByBarCode(String barcode);
}
