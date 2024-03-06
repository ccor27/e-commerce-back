package com.ccor.ecommerce.service;

import com.ccor.ecommerce.exceptions.CancelSaleException;
import com.ccor.ecommerce.model.CanceledSale;
import com.ccor.ecommerce.model.dto.CanceledSaleResponseDTO;
import com.ccor.ecommerce.repository.CanceledSaleRepository;
import com.ccor.ecommerce.service.mapper.CanceledSaleDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CanceledSaleServiceImpService implements ICanceledSaleService {

    private CanceledSaleRepository canceledSaleRepository;
    private CanceledSaleDTOMapper canceledSaleDTOMapper;
    @Autowired
    public CanceledSaleServiceImpService(CanceledSaleRepository canceledSaleRepository, CanceledSaleDTOMapper canceledSaleDTOMapper) {
        this.canceledSaleRepository = canceledSaleRepository;
        this.canceledSaleDTOMapper = canceledSaleDTOMapper;
    }

    @Override
    public CanceledSaleResponseDTO save(CanceledSale canceledSale) {
        if(canceledSale!=null){
            return canceledSaleDTOMapper.apply(canceledSaleRepository.save(canceledSale));
        }else{
            throw new CancelSaleException("The canceled sale is null, therefore is not possible save it");
        }
    }

    @Override
    public CanceledSaleResponseDTO findById(Long id) {
        CanceledSale c = canceledSaleRepository.findById(id).orElse(null);
        if(c!=null){
            return canceledSaleDTOMapper.apply(c);
        }else{
            throw new CancelSaleException("The canceled sale doesn't exist");
        }
    }

    @Override
    public List<CanceledSaleResponseDTO> findAll(Integer offset, Integer pageSize) {
        Page<CanceledSale> list = canceledSaleRepository.findAll(PageRequest.of(offset,pageSize));
        if(list!=null && !list.isEmpty()){

            int totalCanceledSales = canceledSaleRepository.countCanceledSales();
            int adjustedOffset = pageSize*offset;
            adjustedOffset = Math.min(adjustedOffset,totalCanceledSales);
            if(adjustedOffset>=totalCanceledSales){
                throw new CancelSaleException("There are not enough entities to show in a list");
            }else{
                return list.stream().map(canceledSale -> {
                    return canceledSaleDTOMapper.apply(canceledSale);
                }).collect(Collectors.toList());
            }
        }else{
            throw new CancelSaleException("There are not entities to show");
        }
    }

    @Override
    public void remove(Long id) {
        CanceledSale c = canceledSaleRepository.findById(id).orElse(null);
        if(c!=null){
            canceledSaleRepository.delete(c);
        }else{
            throw new CancelSaleException("The canceled sale to remove doesn't exist");
        }
    }

    @Override
    public List<CanceledSale> findAllToExport(Integer offset, Integer pageSize) {
        return canceledSaleRepository.findAll(PageRequest.of(offset,pageSize)).getContent();
    }

    @Override
    public List<CanceledSale> findAllToExport() {
        return canceledSaleRepository.findAll();
    }
}
