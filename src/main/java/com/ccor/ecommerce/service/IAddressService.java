package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.dto.AddressRequestDTO;
import com.ccor.ecommerce.model.dto.AddressResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IAddressService {
    AddressResponseDTO save(AddressRequestDTO addressRequestDTO);
    AddressResponseDTO edit(AddressRequestDTO addressRequestDTO, Long id);
    boolean remove(Long id);
    AddressResponseDTO findById(Long id);
    List<AddressResponseDTO> findAll();
    List<AddressResponseDTO> findAddressesByPostalCode(String postalCode);
    List<AddressResponseDTO> findAddressesByCountry(String country);
}
