package com.ccor.ecommerce.service;

import com.ccor.ecommerce.exceptions.AddressException;
import com.ccor.ecommerce.exceptions.HistoryException;
import com.ccor.ecommerce.model.Address;
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
    private HistoryRepository historyRepository;
    private HistoryDTOMapper historyDTOMapper;
    private SaleDTOMapper saleDTOMapper;
    private SaleRepository saleRepository;
    @Autowired
    public HistoryServiceImp(HistoryRepository historyRepository, HistoryDTOMapper historyDTOMapper,
                             SaleDTOMapper saleDTOMapper, SaleRepository saleRepository) {
        this.historyRepository = historyRepository;
        this.historyDTOMapper = historyDTOMapper;
        this.saleDTOMapper = saleDTOMapper;
        this.saleRepository = saleRepository;
    }

    @Override
    @Transactional
    public HistoryResponseDTO save(HistoryRequestDTO historyRequestDTO) {
        if(historyRequestDTO!=null){
            History history = new History().builder()
                    //.id(null)
                    .customerFullName("")
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
        if(list!=null && !list.isEmpty()){

            int totalProducts = historyRepository.countHistory();
            int adjustedOffset = pageSize*offset;
            adjustedOffset = Math.min(adjustedOffset,totalProducts);
            if(adjustedOffset>=totalProducts){
                throw new AddressException("There aren't the enough histories");
            }else {
                return list.stream().map(history -> {
                    return historyDTOMapper.apply(history);
                }).collect(Collectors.toList());
            }
        }else{
            throw new HistoryException("The list of histories is null");
        }
    }

    @Override
    public List<History> findAllToExport(Integer offset, Integer pageSize) {
        return historyRepository.findAll(PageRequest.of(offset,pageSize)).getContent();
    }

    @Override
    public List<History> findAllToExport() {
        return historyRepository.findAll();
    }

    @Override
    @Transactional
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
