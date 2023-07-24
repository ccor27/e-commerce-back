package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.*;
import com.ccor.ecommerce.model.dto.*;
import com.ccor.ecommerce.repository.*;
import com.ccor.ecommerce.service.mapper.AddressDTOMapper;
import com.ccor.ecommerce.service.mapper.CreditCardDTOMapper;
import com.ccor.ecommerce.service.mapper.CustomerDTOMapper;
import com.ccor.ecommerce.service.mapper.HistoryDTOMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponseDTO save(CustomerRequestDTO requestDTO) {
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
            customer.setPwd(passwordEncoder.encode(requestDTO.pwd()));
            //TODO: send the confirmation and encrypt the password
            String jwtToken = jwtService.generateToken(customer);
            saveCustomerToken(customer,jwtToken);
            return new AuthenticationResponseDTO(jwtToken);
        }else{
            return null;
        }
    }
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
            return null;
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

    @Override
    public boolean remove(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            customer.setHistory(null);
            customerRepository.delete(customer);
            return true;
        } else {
            return false;
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
            return null;
        }
    }

    @Override
    public CustomerResponseDTO findById(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer!=null){
            return customerDTOMapper.apply(customer);
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
            return customerRepository.findCustomerAddress(id).stream().map(address -> {
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
            return customerRepository.findCustomerCreditCards(id).stream().map(card -> {
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
                 Address addressFound = addressRepository.findById(dto.id()).orElse(null);
                 if(addressFound!=null){
                     customer.getAddress().add(addressFound);
                     Customer customerEdited = customerRepository.save(customer);
                     return customerEdited.getAddress().stream().map(address -> {
                         return addressDTOMapper.apply(address);
                     }).collect(Collectors.toList());
                 }else{
                     System.out.println("address doesn't exist");
                     return null;
                 }
            }else{
                //the address doesn't exist
                Address a = new Address(null,dto.street(),dto.country(),dto.postalCode());
                customer.getAddress().add(a);
                Customer customerEdited = customerRepository.save(customer);
                return customerEdited.getAddress().stream().map(address -> {
                    return addressDTOMapper.apply(address);
                }).collect(Collectors.toList());
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
            customerRepository.save(customer);
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
                CreditCard cardFound = creditCardRepository.findById(creditCardResponseDTO.id()).orElse(null);
                if(cardFound!=null){
                    customer.getCards().add(cardFound);
                    Customer customerEdited = customerRepository.save(customer);
                    return customerEdited.getCards().stream().map(card -> {
                        return creditCardDTOMapper.apply(card);
                    }).collect(Collectors.toList());
                }else{
                  return null;
                }
            }else{
                //card doesn't exist
                CreditCard newCard = new CreditCard(null,creditCardResponseDTO.number(),typeCard(creditCardResponseDTO.type()));
                customer.getCards().add(newCard);
                Customer customerEdited = customerRepository.save(customer);
                return customerEdited.getCards().stream().map(card -> {
                    return creditCardDTOMapper.apply(card);
                }).collect(Collectors.toList());
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
            customerRepository.save(customer);
            return true;
        }else{
            return false;
        }
    }
}
