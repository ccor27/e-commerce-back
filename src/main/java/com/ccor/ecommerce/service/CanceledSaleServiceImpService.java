package com.ccor.ecommerce.service;

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

    @Autowired
    private CanceledSaleRepository canceledSaleRepository;
    @Autowired
    private CanceledSaleDTOMapper canceledSaleDTOMapper;

    @Override
    public CanceledSaleResponseDTO save(CanceledSale canceledSale) {
        if(canceledSale!=null){
            return canceledSaleDTOMapper.apply(canceledSaleRepository.save(canceledSale));
        }else{
            return null;
        }
    }

    @Override
    public CanceledSaleResponseDTO findById(Long id) {
        CanceledSale c = canceledSaleRepository.findById(id).orElse(null);
        if(c!=null){
            return canceledSaleDTOMapper.apply(c);
        }else{
            return null;
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
                //error
                return null;
            }else{
                return list.stream().map(canceledSale -> {
                    return canceledSaleDTOMapper.apply(canceledSale);
                }).collect(Collectors.toList());
            }
        }else{
            return null;
        }
    }

    @Override
    public void remove(Long id) {
        CanceledSale c = canceledSaleRepository.findById(id).orElse(null);
        if(c!=null){
            canceledSaleRepository.delete(c);
        }else{
            //error
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
