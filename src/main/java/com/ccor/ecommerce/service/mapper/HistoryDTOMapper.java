package com.ccor.ecommerce.service.mapper;

import com.ccor.ecommerce.model.History;
import com.ccor.ecommerce.model.Sale;
import com.ccor.ecommerce.model.dto.HistoryResponseDTO;
import com.ccor.ecommerce.model.dto.SaleResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class HistoryDTOMapper implements Function<History, HistoryResponseDTO> {
    @Autowired
    private SaleDTOMapper saleDTOMapper;
    @Override
    public HistoryResponseDTO apply(History history) {
        return new HistoryResponseDTO(
                history.getId(),
                saleResponseDTOS(history.getSales()),
                history.getModificationDate()
        );
    }
    private List<SaleResponseDTO> saleResponseDTOS(List<Sale> sales){
        if(sales!=null || sales.size()>0){
            return sales.stream().map(sale -> {
                return saleDTOMapper.apply(sale);
            }).collect(Collectors.toList());
        }else{
            return null;
        }
    }
}
