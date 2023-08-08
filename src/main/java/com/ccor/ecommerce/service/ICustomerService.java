package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.dto.*;

import java.util.List;

public interface ICustomerService {

    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO);
    CustomerResponseDTO getCustomerByToken(String token);
    boolean removeCustomer(Long id);
    CustomerResponseDTO editData(CustomerRequestEditDTO responseDTO, Long id);
    CustomerResponseDTO findById(Long id);
    List<CustomerResponseDTO> findAll(Integer offset, Integer pageSize);
    HistoryResponseDTO findHistory(Long id);
    List<AddressResponseDTO> findAddress(Long id,Integer offset, Integer pageSize);
    List<CreditCardResponseDTO> findCards(Long id,Integer offset, Integer pageSize);
    CustomerResponseDTO findByEmail(String email);
    CustomerResponseDTO changePwd(String pwd, Long id);
    List<AddressResponseDTO> addAddress(AddressResponseDTO addressRequestDTO, Long id);
    boolean removeAddress(Long id_address, Long id_customer);
    List<CreditCardResponseDTO> addCreditCard(CreditCardResponseDTO creditCardResponseDTO, Long id);
    boolean removeCreditCard(Long id_creditCard, Long id_customer);
}
