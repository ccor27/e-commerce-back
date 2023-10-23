package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.dto.*;

import java.util.List;

public interface ISaleService {
    SaleResponseDTO save(SaleRequestDTO saleRequestDTO);
    SaleResponseDTO edit(SaleRequestDTO saleRequestDTO,Long id);
    SaleResponseDTO findById(Long id);
    List<SaleResponseDTO> findAll(Integer offset,Integer pageSize);
    SaleResponseDTO addProductSold(ProductSoldRequestDTO productSoldRequestDTO, Long id);
    SaleResponseDTO removeProductSold(Long id_product, Long id_sale);
    List<ProductSoldResponseDTO> findProductsSold(Long id,Integer offset,Integer pageSize);
    PaymentResponseDTO findPayment(Long id);
}
