package com.ccor.ecommerce.service;

import com.ccor.ecommerce.exceptions.CustomerException;
import com.ccor.ecommerce.model.*;
import com.ccor.ecommerce.model.dto.*;
import com.ccor.ecommerce.repository.*;
import com.ccor.ecommerce.service.mapper.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private SaleDTOMapper saleDTOMapper;
    @Autowired
    private PaymentDTOMapper paymentDTOMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
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
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO) {
        String username =authenticationRequestDTO.username();
        String pwd = authenticationRequestDTO.password();
        Customer customer = customerRepository.findCustomerByUsernameAndIsNotDeleted(username).orElse(null);
        if(customer!=null){
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,pwd
                    )
            );
            String jwtToken = jwtService.generateToken(customer);
            revokeAllCustomerTokens(customer);
            saveCustomerToken(customer,jwtToken);
            return new AuthenticationResponseDTO(jwtToken);
        }else{
            return null;
        }

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
    @Override
    public boolean removeCustomer(Long id) {
        Customer customer = customerRepository.findCustomerByIdAndIsNotDeleted(id).orElse(null);
        if (customer!=null) {
            if(customer.getTokens()!=null){
                customer.getTokens().forEach(token -> token.setCustomer(null));
            }
            if(customer.getConfirmationTokens()!=null){
                customer.getConfirmationTokens().forEach(confirmationToken -> confirmationToken.setCustomer(null));
            }
            customer.setHistory(null);
            customer.setDeleted(true);
            customer.setEnable(false);
            //delete the customer's addresses and customer's cards
            customer.getAddress().clear();
            customer.getCards().clear();
            customerRepository.save(customer);
            return true;
        } else {
            throw new CustomerException("The customer fetched to delete doesn't exist");
        }
    }

    @Override
    public CustomerResponseDTO editData(CustomerRequestEditDTO responseDTO, Long id) {
        Customer customer = customerRepository.findCustomerByIdAndIsNotDeleted(id).orElse(null);
        if(customer!=null){
            customer.setName(responseDTO.name());
            customer.setLastName(responseDTO.lastName());
            customer.setEmail(responseDTO.email());
            customer.setCellphone(responseDTO.cellphone());
            customer.setChannelNotifications(channels(responseDTO.channels()));
            customer.setReceiveNotifications(responseDTO.receiveNotifications());
            return customerDTOMapper.apply(customerRepository.save(customer));
        }else{
            throw new CustomerException("The customer fetched to update doesn't exist");
        }
    }
    private List<ChannelNotification> channels(List<String> channels){
        if(!channels.isEmpty()){
            return channels.stream().map(channel-> {
                switch (channel){
                    case "EMAIL":
                        return ChannelNotification.EMAIL;
                    case "SMS":
                        return ChannelNotification.SMS;
                    default: return null;
                }
            }).collect(Collectors.toList());
        }else{
            return null;
        }
    }
    @Override
    public CustomerResponseDTO findById(Long id) {
        Customer customer = customerRepository.findCustomerByIdAndIsNotDeleted(id).orElse(null);
        if(customer!=null){
            return customerDTOMapper.apply(customer);
        }else{
            throw new CustomerException("The customer fetched by id doesn't exist");
        }
    }

    @Override
    public List<CustomerResponseDTO> findAll(Integer offset, Integer pageSize) {
        Page<Customer> list = customerRepository.findAllNotDeleted(PageRequest.of(offset,pageSize));
        if(list!=null && !list.isEmpty()){
            return list.getContent().stream().map(customer -> {
                return customerDTOMapper.apply(customer);
            }).collect(Collectors.toList());
        }else{
            throw new CustomerException("The list of customers is null");
        }
    }

    @Override
    public List<Customer> findAllToExport(Integer offset, Integer pageSize) {
        return customerRepository.findAll(PageRequest.of(offset,pageSize)).getContent();
    }

    @Override
    public List<Customer> findAllToExport() {
        return customerRepository.findAll();
    }

    @Override
    @Transactional
    public HistoryResponseDTO findHistory(Long id) {
        Customer customer = customerRepository.findCustomerByIdAndIsNotDeleted(id).orElse(null);
        if(customer!=null && customer.getHistory()!=null){
            return historyDTOMapper.apply(customer.getHistory());
        }else{
            throw new CustomerException("The customer's history doesn't exist");
        }
    }

    @Override
    public List<SaleResponseDTO> findCustomerSales(Long id) {
        if(customerRepository.existsById(id)){
            List<Sale> list = customerRepository.findCustomerSales(id);
            if(!list.isEmpty()){
                return list.stream().map(sale -> {
                    return saleDTOMapper.apply(sale);
                }).collect(Collectors.toList());
            }else{
                throw new CustomerException("The customer hasn't have sales");
            }
        }else{
            throw new CustomerException("The customer doesn't exist, therefore is not possible fetch it's sales");
        }
    }

    @Override
    public List<PaymentResponseDTO> findCustomerPayments(Long id) {
        if(customerRepository.existsById(id)){
            List<Payment> list = customerRepository.findCustomerPayments(id);
            if(!list.isEmpty()){
                return list.stream().map(sale -> {
                    return paymentDTOMapper.apply(sale);
                }).collect(Collectors.toList());
            }else{
                throw new CustomerException("The customer hasn't have payments");
            }
        }else{
            throw new CustomerException("The customer doesn't exist, therefore is not possible fetch it's payments");
        }
    }

    @Override
    @Transactional
    public List<AddressResponseDTO> findAddress(Long id,Integer offset, Integer pageSize) {
        Customer customer = customerRepository.findCustomerByIdAndIsNotDeleted(id).orElse(null);
        if(customer!=null && customer.getAddress()!=null){

            int totalAddresses = customerRepository.countByAddressByCustomerAndIsDeletedFalse(id);
            int adjustedOffset = pageSize*offset;
            adjustedOffset = Math.min(adjustedOffset,totalAddresses);
            if(adjustedOffset>=totalAddresses){
                throw new CustomerException("There aren't the enough addresses");
            }else {
                Page<Address> list = customerRepository.findCustomerAddress(id, PageRequest.of(offset, pageSize));
                return list.stream().map(address -> {
                    return addressDTOMapper.apply(address);
                }).collect(Collectors.toList());
            }
        }else{
            throw new CustomerException("The list of customer's address is null");
        }
    }

    @Override
    @Transactional
    public List<CreditCardResponseDTO> findCards(Long id,Integer offset, Integer pageSize) {
        Customer customer = customerRepository.findCustomerByIdAndIsNotDeleted(id).orElse(null);
        if(customer!=null && customer.getCards()!=null){

            int totalCards = customerRepository.countByCreditCardsByCustomerAndIsDeletedFalse(id);
            int adjustedOffset = pageSize*offset;
            adjustedOffset = Math.min(adjustedOffset,totalCards);
            if(adjustedOffset>=totalCards){
                throw new CustomerException("There aren't the enough addresses");
            }else {
                Page<CreditCard> list = customerRepository.findCustomerCreditCards(id, PageRequest.of(offset, pageSize));
                return list.stream().map(card -> {
                    return creditCardDTOMapper.apply(card);
                }).collect(Collectors.toList());
            }
        }else{
            throw new CustomerException("The list of customer's cards is null");
        }
    }

    @Override
    public CustomerResponseDTO findByEmail(String email) {
        Customer customer = customerRepository.findCustomerByEmailAndIsNotDeleted(email).orElse(null);
        if(customer!=null){
            return customerDTOMapper.apply(customer);
        }else{
            throw new CustomerException("The customer fetched by email doesn't exist");
        }
    }
    @Override
    public CustomerResponseDTO changePwd(ChangePwdRequestDTO changePwdRequestDTO) {
        Customer customer = customerRepository.findCustomerByIdAndIsNotDeleted(changePwdRequestDTO.id()).orElse(null);
        if(customer!=null){
            String enteredCurrentPassword =customer.getPwd();
            if(!passwordEncoder.matches(changePwdRequestDTO.currentPassword(),enteredCurrentPassword)){
                throw new CustomerException("The passwords doesn't match, therefore is not possible change it");
            }
            customer.setPwd(passwordEncoder.encode(changePwdRequestDTO.newPassword()));
            return customerDTOMapper.apply(customerRepository.save(customer));
        }else{
            throw new CustomerException("The customer fetched to change its pwd doesn't exist");
        }
    }

    @Override
    public CustomerResponseDTO changeUsername(ChangeUsernameRequestDTO changeUsernameRequestDTO) {
        Customer customer = customerRepository.findCustomerByIdAndIsNotDeleted(changeUsernameRequestDTO.id()).orElse(null);
        if(customer!=null){
            //validate if any other customer has that username
            if(customerRepository.findCustomerByUsername(changeUsernameRequestDTO.newUsername()).isPresent()){
                throw new CustomerException("The new username exist, therefore is not possible use it");
            }
            customer.setUsername(changeUsernameRequestDTO.newUsername());
            return customerDTOMapper.apply(customerRepository.save(customer));
        }else{
            throw new CustomerException("The customer fetched to change its username doesn't exist");
        }
    }

    //in the methods add address, remove address, add card and remove card only return pageable with 10 elements in the 0 page
    @Override
    @Transactional
    public List<AddressResponseDTO> addAddress(AddressResponseDTO dto, Long id) {
        Customer customer = customerRepository.findCustomerByIdAndIsNotDeleted(id).orElse(null);
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
            Page<Address> list = customerRepository.findCustomerAddress(id, PageRequest.of(0,10));
                return list.getContent().stream().map(address -> {
                    return addressDTOMapper.apply(address);
                }).collect(Collectors.toList());

        }else{
            throw new CustomerException("The customer or the address to add doesn't exist");
        }
    }

    @Override
    @Transactional
    public AddressResponseDTO updateDataAddress(Long idCustomer, Long idAddress, AddressEditRequestDTO requestDTO) {
      Address address = addressRepository.findById(idAddress).orElse(null);
      Customer customer = customerRepository.findCustomerByIdAndIsNotDeleted(idCustomer).orElse(null);
      if(address!=null && customer!=null){
          if(customer.getAddress().contains(address)){
              address.setCountry(requestDTO.country());
              address.setStreet(requestDTO.street());
              address.setPostalCode(requestDTO.postalCode());
              addressRepository.save(address);
              return addressDTOMapper.apply(address);
          }else{
              throw new CustomerException("The address doesn't belong to the customer!");
          }
      }else{
          throw new CustomerException("The customer or the address doesn't exist");
      }
    }


    @Override
    @Transactional
    public boolean removeAddress(Long id_address, Long id_customer) {
        Address address = addressRepository.findById(id_address).orElse(null);
        Customer customer = customerRepository.findCustomerByIdAndIsNotDeleted(id_customer).orElse(null);
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
        Customer customer = customerRepository.findCustomerByIdAndIsNotDeleted(id).orElse(null);
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
                customer.getCards().
                        add(new CreditCard(
                                null,
                                customer.getName(),
                                creditCardResponseDTO.number(),
                                creditCardResponseDTO.monthExp(),
                                creditCardResponseDTO.yearExp(),
                                creditCardResponseDTO.cvv(),
                                typeCard(creditCardResponseDTO.type())
                        ));
            }
            customerRepository.save(customer);
            Page<CreditCard> list = customerRepository.findCustomerCreditCards(customer.getId(), PageRequest.of(0,10));
            return list.getContent().stream().map(card -> {
                return creditCardDTOMapper.apply(card);
            }).collect(Collectors.toList());
        }else{
            throw new CustomerException("The customer or the card to add doesn't exist");
        }
    }

    @Override
    @Transactional
    public CreditCardResponseDTO updateDataCreditCard(Long idCustomer ,Long idCard, CreditCardEditRequestDTO creditCardEditRequestDTO) {
        CreditCard card = creditCardRepository.findById(idCard).orElse(null);
        Customer customer = customerRepository.findById(idCustomer).orElse(null);
        if(card!=null && customer!=null){
            if(customer.getCards().contains(card)){
                card.setNumber(creditCardEditRequestDTO.number());
                TypeCard typeCard = typeCard(creditCardEditRequestDTO.type());
                card.setTypeCard(typeCard);
                creditCardRepository.save(card);
                return creditCardDTOMapper.apply(card);
            }else {
                throw new CustomerException("The card doesn't belong to the customer!");
            }
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
        Customer customer = customerRepository.findCustomerByIdAndIsNotDeleted(id_customer).orElse(null);
        if(customer!=null && card!=null){
            customer.getCards().remove(card);
            customerRepository.save(customer);
            return true;
        }else{
            throw new CustomerException("The customer or the card to remove doesn't exist");
        }
    }



}
