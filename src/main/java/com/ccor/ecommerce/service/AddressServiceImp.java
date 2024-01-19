package com.ccor.ecommerce.service;

import com.ccor.ecommerce.exceptions.AddressException;
import com.ccor.ecommerce.exceptions.CustomerException;
import com.ccor.ecommerce.model.Address;
import com.ccor.ecommerce.model.Customer;
import com.ccor.ecommerce.model.dto.AddressEditRequestDTO;
import com.ccor.ecommerce.model.dto.AddressRequestDTO;
import com.ccor.ecommerce.model.dto.AddressResponseDTO;
import com.ccor.ecommerce.repository.AddressRepository;
import com.ccor.ecommerce.repository.CustomerRepository;
import com.ccor.ecommerce.service.mapper.AddressDTOMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImp implements IAddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AddressDTOMapper addressDTOMapper;

    @Override
    public AddressResponseDTO save(AddressRequestDTO addressRequestDTO) {
        if (addressRequestDTO != null) {
                Address newAddress = new Address(
                        null,
                        addressRequestDTO.street(),
                        addressRequestDTO.country(),
                        addressRequestDTO.postalCode()
                );
                Address addressSaved = addressRepository.save(newAddress);
                return addressDTOMapper.apply(addressSaved);
            }
         else {
            throw new AddressException("The address to save is null");
        }
    }


    @Override
    public AddressResponseDTO edit(AddressEditRequestDTO requestDTO, Long id) {
        Address address = addressRepository.findById(id).orElse(null);
        if(address!=null && requestDTO!=null){
            address.setCountry(requestDTO.country());
            address.setStreet(requestDTO.street());
            address.setPostalCode(requestDTO.postalCode());
            return addressDTOMapper.apply(addressRepository.save(address));
        }else{
            throw new AddressException("The address to update doesn't exist or the request is null");
        }
    }

    @Override
    @Transactional
    public boolean remove(Long id) {
        Address address = addressRepository.findById(id).orElse(null);
        if(address!=null){
            List<Customer> listCustomers = customerRepository.findCustomersByAddressId(id);
            for (Customer customer:listCustomers) {
                customer.getAddress().remove(address);
                customerRepository.save(customer);
            }
            addressRepository.deleteById(id);
            return true;
        }else{
            throw new AddressException("The address to delete doesn't exist");
        }
    }

    @Override
    public AddressResponseDTO findById(Long id) {
        Address address = addressRepository.findById(id).orElse(null);
        if(address!=null){
            return addressDTOMapper.apply(address);
        }else{
            throw new AddressException("The address fetched by id doesn't exist");
        }
    }

    @Override
    public List<AddressResponseDTO> findAll(Integer offset,Integer pageSize) {

        int totalAddresses = addressRepository.countAddresses();
        int adjustedOffset = pageSize*offset;
        adjustedOffset = Math.min(adjustedOffset,totalAddresses);
        if(adjustedOffset>=totalAddresses){
            throw new AddressException("There aren't the enough addresses");
        }else {
            Page<Address> list = addressRepository.findAll(PageRequest.of(offset,pageSize));
            if(list!=null && !list.isEmpty()){
                return list.getContent().stream().map(address -> {
                    return addressDTOMapper.apply(address);
                }).collect(Collectors.toList());
            }else{
                throw new AddressException("The list of addresses fetched is null");
            }
        }

    }

    @Override
    public List<Address> findAllToExport(Integer offset, Integer pageSize) {
        return addressRepository.findAll(PageRequest.of(offset,pageSize)).getContent();
    }

    @Override
    public List<Address> findAllToExport() {
        return addressRepository.findAll();
    }

    @Override
    public List<AddressResponseDTO> findAddressesByPostalCode(Integer offset,Integer pageSize,String postalCode) {
        Page<Address> list = addressRepository.findAddressesByPostalCode(PageRequest.of(offset,pageSize),postalCode);
        if(list!=null && !list.isEmpty()){
            return list.getContent()
                    .stream().map(address -> {
                return addressDTOMapper.apply(address);
            }).collect(Collectors.toList());
        }else{
            throw new AddressException("The list of addresses fetched by postal code is null");
        }
    }

    @Override
    public List<AddressResponseDTO> findAddressesByCountry(Integer offset,Integer pageSize, String country) {
        Page<Address> list = addressRepository.findAddressesByCountry(PageRequest.of(offset,pageSize),country);
        if(list!=null){
            return list
                    .stream().map(address -> {
                        return addressDTOMapper.apply(address);
                    }).collect(Collectors.toList());
        }else{
            throw new AddressException("The list of addresses fetched by country code is null");
        }
    }
}
