package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.Customer;
import com.ccor.ecommerce.model.dto.*;

import java.util.List;

public interface ICustomerService {

    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO);
    CustomerResponseDTO getCustomerByToken(String token);
    boolean removeCustomer(Long id);
    CustomerResponseDTO editData(CustomerRequestEditDTO responseDTO, Long id);
    CustomerResponseDTO findById(Long id);
    List<CustomerResponseDTO> findAll(Integer offset, Integer pageSize);
    List<Customer> findAllToExport(Integer offset, Integer pageSize);
    List<Customer> findAllToExport();
    HistoryResponseDTO findHistory(Long id);
    List<SaleResponseDTO> findCustomerSales(Long id);
    List<PaymentResponseDTO> findCustomerPayments(Long id);
    List<AddressResponseDTO> findAddress(Long id,Integer offset, Integer pageSize);
    List<CreditCardResponseDTO> findCards(Long id,Integer offset, Integer pageSize);
    CustomerResponseDTO findByEmail(String email);
    CustomerResponseDTO changePwd(ChangePwdRequestDTO changePwdRequestDTO);
    CustomerResponseDTO changeUsername(ChangeUsernameRequestDTO changeUsernameRequestDTO);
    List<AddressResponseDTO> addAddress(AddressResponseDTO addressRequestDTO, Long id);
    AddressResponseDTO updateDataAddress(Long idCustomer, Long idAddress, AddressEditRequestDTO requestDTO);
    boolean removeAddress(Long id_address, Long id_customer);
    List<CreditCardResponseDTO> addCreditCard(CreditCardResponseDTO creditCardResponseDTO, Long id);
    CreditCardResponseDTO updateDataCreditCard(Long idCustomer, Long idCard, CreditCardEditRequestDTO creditCardEditRequestDTO);
    boolean removeCreditCard(Long id_creditCard, Long id_customer);

}
