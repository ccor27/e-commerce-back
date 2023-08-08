package com.ccor.ecommerce.service;

import com.ccor.ecommerce.exceptions.HistoryException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
            throw new HistoryException("The request to save is null");
        }
    }

    @Override
    public boolean remove(Long id) {
     if(historyRepository.existsById(id)){
         historyRepository.deleteById(id);
         return true;
     }else{
         throw new HistoryException("The history fetched to delete doesn't exist");
     }
    }

    @Override
    public HistoryResponseDTO findById(Long id) {
        History history = historyRepository.findById(id).orElse(null);
        if (history!=null){
            return historyDTOMapper.apply(history);
        }else{
            throw new HistoryException("The history fetched by id doesn't exist");
        }
    }

    @Override
    public List<HistoryResponseDTO> findAll(Integer offset, Integer pageSize) {
        Page<History> list = historyRepository.findAll(PageRequest.of(offset,pageSize));
        if(list!=null){
            return list.stream().map(history -> {
                return historyDTOMapper.apply(history);
            }).collect(Collectors.toList());
        }else{
            throw new HistoryException("The list of histories is null");
        }
    }

    @Override
    public List<SaleResponseDTO> findSales(Long id,Integer offset, Integer pageSize) {
        Page<Sale> list = historyRepository.findHistorySales(id,PageRequest.of(offset,pageSize));
        if(list!=null ){
            return list.stream().map(sale -> {
                return saleDTOMapper.apply(sale);
            }).collect(Collectors.toList());
        }else{
            throw new HistoryException("The list of history's sales is null");
        }
    }
    //TODO: this method return a pageable with 10 elements in the 0 page
    @Override
    @Transactional
    public List<SaleResponseDTO> addSale(SaleResponseDTO saleResponseDTO, Long id) {
        History history = historyRepository.findById(id).orElse(null);
        Sale sale = saleRepository.findById(saleResponseDTO.id()).orElse(null);
        if(history!=null && sale!=null){
            history.getSales().add(sale);
            history.setModificationDate(new Date());
            historyRepository.save(history);
            Page<Sale> list = historyRepository.findHistorySales(history.getId(), PageRequest.of(0,10));
            return list.stream().map(s -> {
                return saleDTOMapper.apply(s);
            }).collect(Collectors.toList());
        }else{
            throw new HistoryException("The history fetched or the sale to add doesn't exist");
        }

    }

    @Override
    @Transactional
    public boolean removeSale(Long id_sale, Long id_history) {
        History history = historyRepository.findById(id_history).orElse(null);
        Sale sale = saleRepository.findById(id_sale).orElse(null);
        if(history!=null && sale!=null){
            history.getSales().remove(sale);
            history.setModificationDate(new Date());
            historyRepository.save(history);
            return true;
        }else{
            throw new HistoryException("The history fetched or the sale fetched doesn't exist");
        }
    }
}
