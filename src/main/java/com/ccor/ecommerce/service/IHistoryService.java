package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.dto.HistoryRequestDTO;
import com.ccor.ecommerce.model.dto.HistoryResponseDTO;
import com.ccor.ecommerce.model.dto.SaleResponseDTO;

import java.util.List;

public interface IHistoryService {
    HistoryResponseDTO save(HistoryRequestDTO historyRequestDTO);
    boolean remove(Long id);
    HistoryResponseDTO findById(Long id);
    List<HistoryResponseDTO> findAll(Integer offset, Integer pageSize);
    List<SaleResponseDTO> findSales(Long id,Integer offset, Integer pageSize);
    List<SaleResponseDTO> addSale(SaleResponseDTO saleResponseDTO, Long id);
    boolean removeSale(Long id_sale, Long id_history);
}
