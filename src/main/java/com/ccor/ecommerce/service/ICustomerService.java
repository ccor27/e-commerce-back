package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.dto.*;

import java.util.List;

public interface ICustomerService {

    AuthenticationResponseDTO save(CustomerRequestDTO requestDTO);
    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO);
    CustomerResponseDTO getCustomerByToken(String token);
    boolean remove(Long id);
    CustomerResponseDTO editData(CustomerRequestEditDTO responseDTO, Long id);
    CustomerResponseDTO findById(Long id);
    List<CustomerResponseDTO> findAll();
    HistoryResponseDTO findHistory(Long id);
    List<AddressResponseDTO> findAddress(Long id);
    List<CreditCardResponseDTO> findCards(Long id);
    CustomerResponseDTO findByEmail(String email);
    CustomerResponseDTO changePwd(String pwd, Long id);
    List<AddressResponseDTO> addAddress(AddressResponseDTO addressRequestDTO, Long id);
    boolean removeAddress(Long id_address, Long id_customer);
    List<CreditCardResponseDTO> addCreditCard(CreditCardResponseDTO creditCardResponseDTO, Long id);
    boolean removeCreditCard(Long id_creditCard, Long id_customer);
}
