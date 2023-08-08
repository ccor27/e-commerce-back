package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.dto.CreditCardRequestDTO;
import com.ccor.ecommerce.model.dto.CreditCardResponseDTO;

import java.util.List;

public interface ICreditCardService {

     CreditCardResponseDTO save(CreditCardRequestDTO requestDTO);
     CreditCardResponseDTO edit(CreditCardRequestDTO requestDTO, Long id);
     CreditCardResponseDTO findById(Long id);
     CreditCardResponseDTO findCardByNumber(String number);
     List<CreditCardResponseDTO> findAll(Integer offset, Integer pageSize);
     List<CreditCardResponseDTO> findCardsByType(Integer offset, Integer pageSize,String type);
     boolean remove(Long id);

}
