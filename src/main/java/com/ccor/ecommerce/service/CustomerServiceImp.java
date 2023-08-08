package com.ccor.ecommerce.service;

import com.ccor.ecommerce.exceptions.CustomerException;
import com.ccor.ecommerce.model.*;
import com.ccor.ecommerce.model.dto.*;
import com.ccor.ecommerce.repository.*;
import com.ccor.ecommerce.service.mapper.AddressDTOMapper;
import com.ccor.ecommerce.service.mapper.CreditCardDTOMapper;
import com.ccor.ecommerce.service.mapper.CustomerDTOMapper;
import com.ccor.ecommerce.service.mapper.HistoryDTOMapper;
import com.ccor.ecommerce.service.registration.ConfirmationTokenServiceImp;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.cert.CertStoreException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImp implements ICustomerService{

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private CustomerDTOMapper customerDTOMapper;
    @Autowired
    private HistoryDTOMapper historyDTOMapper;
    @Autowired
    private AddressDTOMapper addressDTOMapper;
    @Autowired
    private CreditCardDTOMapper creditCardDTOMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ConfirmationTokenServiceImp confirmationTokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;



    private void saveCustomerToken (Customer customer, String jwtToken){
        Token token = new Token(
                null,
                jwtToken,
                TokenType.BEARER,
                customer,
                false,
                false
        );
        tokenRepository.save(token);
    }

    @Override
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO) {
        String username =authenticationRequestDTO.username();
        String pwd = authenticationRequestDTO.password();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,pwd
                )
        );
        Customer customer = customerRepository.findCustomerByUsername(username).orElse(null);
        System.out.println("Roles customer authenticate "+customer.getRoles());
        String jwtToken = jwtService.generateToken(customer);
        revokeAllCustomerTokens(customer);
        saveCustomerToken(customer,jwtToken);
        return new AuthenticationResponseDTO(jwtToken);
    }
    @Override
    public CustomerResponseDTO getCustomerByToken(String token) {
        Customer customer = tokenRepository.getCustomerByToken(token);
        if(customer!=null){
            return customerDTOMapper.apply(customer);
        }else {
           throw new CustomerException("The customer fetched by token doesn't exist");
        }
    }

    private void revokeAllCustomerTokens(Customer customer){
        List<Token> validCustomerTokens = tokenRepository.findAllValidTokenByCustomer(customer.getId());
        if(validCustomerTokens.isEmpty())
            return;
        validCustomerTokens.forEach(t->{
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validCustomerTokens);
    }
    //TODO: search more information about remove customers when they are associated with sales
    @Override
    public boolean removeCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer!=null) {
            customer.getTokens().forEach(token -> token.setCustomer(null));
            customer.getConfirmationTokens().forEach(confirmationToken -> confirmationToken.setCustomer(null));
            customer.setHistory(null);
            customerRepository.delete(customer);
            return true;
        } else {
            throw new CustomerException("The customer fetched to delete doesn't exist");
        }
    }

    @Override
    public CustomerResponseDTO editData(CustomerRequestEditDTO responseDTO, Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer!=null){
            customer.setName(responseDTO.name());
            customer.setLastName(responseDTO.lastName());
            customer.setEmail(responseDTO.email());
            customer.setCellphone(responseDTO.cellphone());
            return customerDTOMapper.apply(customerRepository.save(customer));
        }else{
            throw new CustomerException("The customer fetched to update doesn't exist");
        }
    }

    @Override
    public CustomerResponseDTO findById(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer!=null){
            return customerDTOMapper.apply(customer);
        }else{
            throw new CustomerException("The customer fetched by id doesn't exist");
        }
    }

    @Override
    public List<CustomerResponseDTO> findAll(Integer offset, Integer pageSize) {
        Page<Customer> list = customerRepository.findAll(PageRequest.of(offset,pageSize));
        if(list!=null){
            return list.stream().map(customer -> {
                return customerDTOMapper.apply(customer);
            }).collect(Collectors.toList());
        }else{
            throw new CustomerException("The list of customers is null");
        }
    }
    @Override
    @Transactional
    public HistoryResponseDTO findHistory(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer!=null && customer.getHistory()!=null){
            return historyDTOMapper.apply(customer.getHistory());
        }else{
            throw new CustomerException("The customer's history doesn't exist");
        }
    }

    @Override

    public List<AddressResponseDTO> findAddress(Long id,Integer offset, Integer pageSize) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer!=null && customer.getAddress()!=null){
            Page<Address> list = customerRepository.findCustomerAddress(id,PageRequest.of(offset,pageSize));
            return list.stream().map(address -> {
                return addressDTOMapper.apply(address);
            }).collect(Collectors.toList());
        }else{
            throw new CustomerException("The list of customer's address is null");
        }
    }

    @Override
    @Transactional
    public List<CreditCardResponseDTO> findCards(Long id,Integer offset, Integer pageSize) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer!=null && customer.getCards()!=null){
            Page<CreditCard> list =customerRepository.findCustomerCreditCards(id,PageRequest.of(offset,pageSize));
            return list.stream().map(card -> {
                return creditCardDTOMapper.apply(card);
            }).collect(Collectors.toList());
        }else{
            throw new CustomerException("The list of customer's cards is null");
        }
    }

    @Override
    public CustomerResponseDTO findByEmail(String email) {
        Customer customer = customerRepository.findCustomerByEmail(email).orElse(null);
        if(customer!=null){
            return customerDTOMapper.apply(customer);
        }else{
            throw new CustomerException("The customer fetched by email doesn't exist");
        }
    }
    @Override
    public CustomerResponseDTO changePwd(String pwd, Long id) {
        //TODO: I don't know very well if this is correct
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer!=null){
            System.out.println("old pwd: "+customer.getPwd());
            customer.setPwd(passwordEncoder.encode(pwd));
            System.out.println("new pwd: "+customer.getPwd());
            return customerDTOMapper.apply(customerRepository.save(customer));
        }else{
            throw new CustomerException("The customer fetched to change its pwd doesn't exist");
        }
    }
    //TODO: in the methods add address, remove address, add card and remove card only return pageable with 10 elements in the 0 page
    @Override
    @Transactional
    public List<AddressResponseDTO> addAddress(AddressResponseDTO dto, Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer!=null && dto!=null){
            if(dto.id()!=null){
                //the address exist
                 Address addressFound = addressRepository.findById(dto.id()).orElse(null);
                 if(addressFound!=null){
                     customer.getAddress().add(addressFound);
                 }else{
                     throw new CustomerException("The address to add doesn't exist");
                 }
            }else{
                //the address doesn't exist
                Address a = new Address(null,dto.street(),dto.country(),dto.postalCode());
                customer.getAddress().add(a);
            }
            customerRepository.save(customer);
            Page<Address> list = customerRepository.findCustomerAddress(customer.getId(), PageRequest.of(0,10));
            return list.stream().map(address -> {
                return addressDTOMapper.apply(address);
            }).collect(Collectors.toList());
        }else{
            throw new CustomerException("The customer or the address to add doesn't exist");
        }
    }

    @Override
    @Transactional
    public boolean removeAddress(Long id_address, Long id_customer) {
        Address address = addressRepository.findById(id_address).orElse(null);
        Customer customer = customerRepository.findById(id_customer).orElse(null);
        if(customer!=null && address!=null){
            customer.getAddress().remove(address);
            customerRepository.save(customer);
            return true;
        }else{
            throw new CustomerException("The customer or the address to remove doesn't exist");
        }
    }

    @Override
    @Transactional
    public List<CreditCardResponseDTO> addCreditCard(CreditCardResponseDTO creditCardResponseDTO, Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer!=null && creditCardResponseDTO!=null){
            if(creditCardResponseDTO.id()!=null){
                //card exist
                CreditCard cardFound = creditCardRepository.findById(creditCardResponseDTO.id()).orElse(null);
                if(cardFound!=null){
                    customer.getCards().add(cardFound);
                }else{
                    throw new CustomerException("The card to add doesn't exist");
                }
            }else{
                //card doesn't exist
                CreditCard newCard = new CreditCard(null,creditCardResponseDTO.number(),typeCard(creditCardResponseDTO.type()));
                customer.getCards().add(newCard);
            }
            customerRepository.save(customer);
            Page<CreditCard> list = customerRepository.findCustomerCreditCards(customer.getId(), PageRequest.of(0,10));
            return list.stream().map(card -> {
                return creditCardDTOMapper.apply(card);
            }).collect(Collectors.toList());
        }else{
            throw new CustomerException("The customer or the card to add doesn't exist");
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
            customerRepository.save(customer);
            return true;
        }else{
            throw new CustomerException("The customer or the card to remove doesn't exist");
        }
    }
}
