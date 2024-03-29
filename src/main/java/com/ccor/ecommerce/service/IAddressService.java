package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.Address;
import com.ccor.ecommerce.model.ProductStock;
import com.ccor.ecommerce.model.dto.AddressEditRequestDTO;
import com.ccor.ecommerce.model.dto.AddressRequestDTO;
import com.ccor.ecommerce.model.dto.AddressResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IAddressService {
    AddressResponseDTO save(AddressRequestDTO addressRequestDTO);
    AddressResponseDTO edit(AddressEditRequestDTO requestDTO, Long id);
    boolean remove(Long id);
    AddressResponseDTO findById(Long id);
    List<AddressResponseDTO> findAll(Integer offset, Integer pageSize);
    List<Address> findAllToExport(Integer offset, Integer pageSize);
    List<Address> findAllToExport();
    List<AddressResponseDTO> findAddressesByPostalCode(Integer offset, Integer pageSize,String postalCode);
    List<AddressResponseDTO> findAddressesByCountry(Integer offset, Integer pageSize,String country);
}
