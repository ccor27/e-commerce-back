package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.Customer;
import com.ccor.ecommerce.model.ProductStock;
import com.ccor.ecommerce.model.dto.ProductStockRequestDTO;
import com.ccor.ecommerce.model.dto.ProductStockResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductStockService {
    ProductStockResponseDTO save(ProductStockRequestDTO productStockRequestDTO,MultipartFile file);
    boolean validateNameAndBarCode(String name, String barCode);
    boolean remove(Long id);
    ProductStockResponseDTO edit(ProductStockRequestDTO productStockRequestDTO,MultipartFile file, Long id);
    List<ProductStockResponseDTO> findAll(Integer offset, Integer pageSize);
    List<ProductStock> findAllToExport(Integer offset, Integer pageSize);
    List<ProductStock> findAllToExport();
    ProductStockResponseDTO findProductById(Long id);
    List<ProductStockResponseDTO> findProductStocksByEnableProduct(Integer offset, Integer pageSize);
    ProductStockResponseDTO findProductStockByBarCode(String barCode);
    ProductStockResponseDTO sellProduct(int amountSold, String barCode);
    void addBackAmount(String barcode, int amount);

}
