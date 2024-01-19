package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.Address;
import com.ccor.ecommerce.model.CanceledSale;
import com.ccor.ecommerce.model.dto.CanceledSaleResponseDTO;

import java.util.List;

public interface ICanceledSaleService {

    public CanceledSaleResponseDTO save(CanceledSale canceledSale);
    public CanceledSaleResponseDTO findById(Long id);
    public List<CanceledSaleResponseDTO> findAll(Integer offset, Integer pageSize);
    public void remove(Long id);
    List<CanceledSale> findAllToExport(Integer offset, Integer pageSize);
    List<CanceledSale> findAllToExport();
}
