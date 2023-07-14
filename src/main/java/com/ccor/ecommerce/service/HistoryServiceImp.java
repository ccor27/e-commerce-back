package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.History;
import com.ccor.ecommerce.model.Sale;
import com.ccor.ecommerce.model.dto.HistoryRequestDTO;
import com.ccor.ecommerce.model.dto.HistoryResponseDTO;
import com.ccor.ecommerce.model.dto.ProductSoldResponseDTO;
import com.ccor.ecommerce.model.dto.SaleResponseDTO;
import com.ccor.ecommerce.repository.HistoryRepository;
import com.ccor.ecommerce.repository.SaleRepository;
import com.ccor.ecommerce.service.mapper.HistoryDTOMapper;
import com.ccor.ecommerce.service.mapper.SaleDTOMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryServiceImp implements IHistoryService{
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private HistoryDTOMapper historyDTOMapper;
    @Autowired
    private SaleDTOMapper saleDTOMapper;
    @Autowired
    private SaleRepository saleRepository;
    @Override
    public HistoryResponseDTO save(HistoryRequestDTO historyRequestDTO) {
        if(historyRequestDTO!=null){
            History history = new History().builder()
                    .sales(new ArrayList<>())
                    .modificationDate(historyRequestDTO.dateModification())
                    .build();
            return historyDTOMapper.apply(historyRepository.save(history));
        }else{
            return null;
        }
    }

    @Override
    public boolean remove(Long id) {
     if(historyRepository.existsById(id)){
         historyRepository.deleteById(id);
         return true;
     }else{
         return false;
     }
    }

    @Override
    public HistoryResponseDTO findById(Long id) {
        History history = historyRepository.findById(id).orElse(null);
        if (history!=null){
            return historyDTOMapper.apply(history);
        }else{
            return null;
        }
    }

    @Override
    public List<HistoryResponseDTO> findAll() {
        List<History> list = historyRepository.findAll();
        if(list!=null && !list.isEmpty()){
            return list.stream().map(history -> {
                return historyDTOMapper.apply(history);
            }).collect(Collectors.toList());
        }else{
            return null;
        }
    }

    @Override
    public List<SaleResponseDTO> findSales(Long id) {
        List<Sale> list = historyRepository.findHistorySales(id);
        if(list!=null && !list.isEmpty()){
            return list.stream().map(sale -> {
                return saleDTOMapper.apply(sale);
            }).collect(Collectors.toList());
        }else{
            return null;
        }
    }

    @Override
    @Transactional
    public List<SaleResponseDTO> addSale(SaleResponseDTO saleResponseDTO, Long id) {
        History history = historyRepository.findById(id).orElse(null);
        Sale sale = saleRepository.findById(saleResponseDTO.id()).orElse(null);
        if(history!=null && sale!=null){
            history.getSales().add(sale);
            History historyEdited = historyRepository.save(history);
            return historyEdited.getSales().stream().map(s -> {
                return saleDTOMapper.apply(s);
            }).collect(Collectors.toList());
        }else{
            return null;
        }

    }

    @Override
    @Transactional
    public boolean removeSale(Long id_sale, Long id_history) {
        History history = historyRepository.findById(id_history).orElse(null);
        Sale sale = saleRepository.findById(id_sale).orElse(null);
        if(history!=null && sale!=null){
            history.getSales().remove(sale);
            historyRepository.save(history);
            return true;
        }else{
            return false;
        }
    }
}
