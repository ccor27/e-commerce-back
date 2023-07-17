package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.dto.ProductSoldRequestDTO;
import com.ccor.ecommerce.model.dto.ProductSoldResponseDTO;
import com.ccor.ecommerce.model.dto.SaleRequestDTO;
import com.ccor.ecommerce.model.dto.SaleResponseDTO;

import java.util.List;

public interface ISaleService {
    SaleResponseDTO save(SaleRequestDTO saleRequestDTO);
    SaleResponseDTO edit(SaleRequestDTO saleRequestDTO,Long id);
    SaleResponseDTO findById(Long id);
    List<SaleResponseDTO> findAll();
    SaleResponseDTO addProductSold(ProductSoldRequestDTO productSoldRequestDTO, Long id);
    SaleResponseDTO removeProductSold(Long id_product, Long id_sale);
    List<ProductSoldResponseDTO> findProductsSold(Long id);
}
