package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.*;
import com.ccor.ecommerce.model.dto.*;
import com.ccor.ecommerce.repository.AddressRepository;
import com.ccor.ecommerce.repository.CreditCardRepository;
import com.ccor.ecommerce.repository.CustomerRepository;
import com.ccor.ecommerce.repository.HistoryRepository;
import com.ccor.ecommerce.service.mapper.AddressDTOMapper;
import com.ccor.ecommerce.service.mapper.CreditCardDTOMapper;
import com.ccor.ecommerce.service.mapper.CustomerDTOMapper;
import com.ccor.ecommerce.service.mapper.HistoryDTOMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImp implements ICustomerService{

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerDTOMapper customerDTOMapper;
    @Autowired
    private HistoryDTOMapper historyDTOMapper;
    @Autowired
    private AddressDTOMapper addressDTOMapper;
    @Autowired
    private CreditCardDTOMapper creditCardDTOMapper;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;

    @Override
    public CustomerResponseDTO save(CustomerRequestDTO requestDTO) {
        if(requestDTO!=null){
            Customer customer = Customer.builder()
                    .address(new ArrayList<>())
                    .cards(new ArrayList<>())
                    .history(new History(null,new ArrayList<>(),new Date()))
                    .roles(Arrays.asList(Role.CUSTOMER))
                    .tokens(new ArrayList<>())
                    .confirmationTokens(new ArrayList<>())
                    .enableUser(false)
                    .build();
            customer.setName(requestDTO.name());
            customer.setLastName(requestDTO.lastName());
            customer.setCellphone(requestDTO.cellphone());
            customer.setEmail(requestDTO.email());
            customer.setUsername(requestDTO.username());
            customer.setPwd(requestDTO.pwd());
            //TODO: send the confirmation and encrypt the pwd
            return customerDTOMapper.apply(customerRepository.save(customer));
        }else{
            return null;
        }
    }

    @Override
    public boolean remove(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer!=null){
          customer.setHistory(null);
          customerRepository.delete(customer);
          return true;
        }else{
            return false;
        }
    }

    @Override
    public CustomerResponseDTO editData(CustomerResponseDTO responseDTO, Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer!=null){
            customer.setName(responseDTO.name());
            customer.setLastName(responseDTO.lastName());
            customer.setEmail(responseDTO.email());
            customer.setCellphone(responseDTO.cellphone());
            return customerDTOMapper.apply(customerRepository.save(customer));
        }else{
            return null;
        }
    }

    @Override
    public CustomerResponseDTO findById(Long id) {
        if(customerRepository.existsById(id)){
            return customerDTOMapper.apply(customerRepository.findById(id).get());
        }else{
            return null;
        }
    }

    @Override
    public List<CustomerResponseDTO> findAll() {
        List<Customer> list = customerRepository.findAll();
        if(list!=null){
            return list.stream().map(customer -> {
                return customerDTOMapper.apply(customer);
            }).collect(Collectors.toList());
        }else{
            return null;
        }
    }

    @Override
    @Transactional
    public HistoryResponseDTO findHistory(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer!=null && customer.getHistory()!=null){
            return historyDTOMapper.apply(customer.getHistory());
        }else{
            return null;
        }
    }

    @Override

    public List<AddressResponseDTO> findAddress(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer!=null && customer.getAddress()!=null){
            return customer.getAddress().stream().map(address -> {
                return addressDTOMapper.apply(address);
            }).collect(Collectors.toList());
        }else{
            return null;
        }
    }

    @Override
    @Transactional
    public List<CreditCardResponseDTO> findCards(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer!=null && customer.getCards()!=null){
            return customer.getCards().stream().map(card -> {
                return creditCardDTOMapper.apply(card);
            }).collect(Collectors.toList());
        }else{
            return null;
        }
    }

    @Override
    public CustomerResponseDTO findByEmail(String email) {
        Customer customer = customerRepository.findCustomerByEmail(email).orElse(null);
        if(customer!=null){
            return customerDTOMapper.apply(customer);
        }else{
            return null;
        }
    }

    @Override
    public CustomerResponseDTO changeUsername(String username, Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer!=null){
            customer.setUsername(username);
            return customerDTOMapper.apply(customerRepository.save(customer));
        }else{
            return null;
        }
    }

    @Override
    public CustomerResponseDTO changePwd(String pwd, Long id) {
        //TODO: I don't know very well if this is correct
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer!=null){
            customer.setPwd(pwd);
            return customerDTOMapper.apply(customerRepository.save(customer));
        }else{
            return null;
        }
    }

    @Override
    @Transactional
    public List<AddressResponseDTO> addAddress(AddressResponseDTO dto, Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer!=null && dto!=null){
            if(dto.id()!=null){
                //the address exist
                 Address address = addressRepository.findById(dto.id()).orElse(null);
                 if(address!=null){
                     customer.getAddress().add(address);
                     return findAddress(id);
                 }else{
                     return null;
                 }
            }else{
                //the address doesn't exist
                customer.getAddress().add(new Address(null,dto.street(),dto.country(),dto.postalCode()));
                return findAddress(id);
            }
        }else{
            return null;
        }
    }

    @Override
    @Transactional
    public boolean removeAddress(Long id_address, Long id_customer) {
        Address address = addressRepository.findById(id_address).orElse(null);
        Customer customer = customerRepository.findById(id_customer).orElse(null);
        if(customer!=null && address!=null){
            customer.getAddress().remove(address);
            return true;
        }else{
            return false;
        }
    }

    @Override
    @Transactional
    public List<CreditCardResponseDTO> addCreditCard(CreditCardResponseDTO creditCardResponseDTO, Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer!=null && creditCardResponseDTO!=null){
            if(creditCardResponseDTO.id()!=null){
                //card exist
                CreditCard card = creditCardRepository.findById(creditCardResponseDTO.id()).orElse(null);
                if(card!=null){
                    customer.getCards().add(card);
                    customerRepository.save(customer);
                    return findCards(id);
                }else{
                  return null;
                }
            }else{
                //card doesn't exist
                CreditCard card = new CreditCard(null,creditCardResponseDTO.number(),typeCard(creditCardResponseDTO.type()));
                customer.getCards().add(card);
                return findCards(id);
            }
        }else{
           return null;
        }
    }
    private TypeCard typeCard(String type){
        return switch (type) {
            case "VISA" -> TypeCard.VISA;
            case "MASTER_CARD" -> TypeCard.MASTER_CARD;
            case "AMERICAN_EXPRESS" -> TypeCard.AMERICAN_EXPRESS;
            default -> null;
        };
    }

    @Override
    @Transactional
    public boolean removeCreditCard(Long id_creditCard, Long id_customer) {
        CreditCard card = creditCardRepository.findById(id_creditCard).orElse(null);
        Customer customer = customerRepository.findById(id_customer).orElse(null);
        if(customer!=null && card!=null){
            customer.getCards().remove(card);
            return true;
        }else{
            return false;
        }
    }
}
