package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.History;
import com.ccor.ecommerce.model.dto.HistoryRequestDTO;
import com.ccor.ecommerce.model.dto.HistoryResponseDTO;
import com.ccor.ecommerce.model.dto.SaleResponseDTO;

import java.util.List;

public interface IHistoryService {
    HistoryResponseDTO save(HistoryRequestDTO historyRequestDTO);
    boolean remove(Long id);
    HistoryResponseDTO findById(Long id);
    List<HistoryResponseDTO> findAll(Integer offset, Integer pageSize);
    List<History> findAllToExport(Integer offset, Integer pageSize);
    List<History> findAllToExport();
    List<SaleResponseDTO> findSales(Long id,Integer offset, Integer pageSize);
    boolean removeSale(Long id_sale, Long id_history);
}
