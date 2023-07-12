package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.Address;
import com.ccor.ecommerce.model.dto.AddressRequestDTO;
import com.ccor.ecommerce.model.dto.AddressResponseDTO;
import com.ccor.ecommerce.repository.AddressRepository;
import com.ccor.ecommerce.service.mapper.AddressDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImp implements IAddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressDTOMapper addressDTOMapper;

    @Override
    public AddressResponseDTO save(AddressRequestDTO addressRequestDTO) {
        if (addressRequestDTO != null) {
            Address address = new Address(
                    null,
                    addressRequestDTO.street(),
                    addressRequestDTO.country(),
                    addressRequestDTO.postalCode()
            );
            Address addressSaved = addressRepository.save(address);
            return addressDTOMapper.apply(addressSaved);
        } else {
            return null;
        }
    }


    @Override
    public AddressResponseDTO edit(AddressRequestDTO addressRequestDTO, Long id) {
        Address address = addressRepository.findById(id).orElse(null);
        if(address!=null && addressRequestDTO!=null){
            address.setCountry(addressRequestDTO.country());
            address.setStreet(addressRequestDTO.street());
            address.setPostalCode(addressRequestDTO.postalCode());
            return addressDTOMapper.apply(addressRepository.save(address));
        }else{
            return null;
        }
    }

    @Override
    public boolean remove(Long id) {
        if(addressRepository.existsById(id)){
            addressRepository.deleteById(id);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public AddressResponseDTO findById(Long id) {
        Address address = addressRepository.findById(id).orElse(null);
        return address!=null ? addressDTOMapper.apply(address) : null;
    }

    @Override
    public List<AddressResponseDTO> findAll() {
        if(!addressRepository.findAll().isEmpty()){
            return addressRepository.findAll().stream().map(address -> {
                return addressDTOMapper.apply(address);
            }).collect(Collectors.toList());
        }else{
            return null;
        }
    }

    @Override
    public List<AddressResponseDTO> findAddressesByPostalCode(String postalCode) {
        if(!addressRepository.findAddressesByPostalCode(postalCode).isEmpty()){
            return addressRepository.findAddressesByPostalCode(postalCode)
                    .stream().map(address -> {
                return addressDTOMapper.apply(address);
            }).collect(Collectors.toList());
        }else{
            return null;
        }
    }

    @Override
    public List<AddressResponseDTO> findAddressesByCountry(String country) {
        if(!addressRepository.findAddressesByCountry(country).isEmpty()){
            return addressRepository.findAddressesByCountry(country)
                    .stream().map(address -> {
                        return addressDTOMapper.apply(address);
                    }).collect(Collectors.toList());
        }else{
            return null;
        }
    }
}
