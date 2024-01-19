package com.ccor.ecommerce.service;

import com.ccor.ecommerce.exceptions.CustomerException;
import com.ccor.ecommerce.model.*;
import com.ccor.ecommerce.model.dto.*;
import com.ccor.ecommerce.repository.AddressRepository;
import com.ccor.ecommerce.repository.CreditCardRepository;
import com.ccor.ecommerce.repository.CustomerRepository;
import com.ccor.ecommerce.service.mapper.AddressDTOMapper;
import com.ccor.ecommerce.service.mapper.CreditCardDTOMapper;
import com.ccor.ecommerce.service.mapper.CustomerDTOMapper;
import com.ccor.ecommerce.service.mapper.HistoryDTOMapper;
import com.ccor.ecommerce.service.registration.ConfirmationTokenServiceImp;
import com.ccor.ecommerce.service.registration.EmailValidator;
import com.ccor.ecommerce.service.registration.IEmailSender;
import com.ccor.ecommerce.service.registration.RegistrationServiceImp;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImpTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private CreditCardRepository creditCardRepository;
    @Mock
    private CustomerDTOMapper customerDTOMapper;
    @Mock
    private HistoryDTOMapper historyDTOMapper;
    @Mock
    private AddressDTOMapper addressDTOMapper;
    @Mock
    private CreditCardDTOMapper creditCardDTOMapper;
    @Mock
    private EmailValidator emailValidator;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private IEmailSender emailSender;
    @Mock
    private ConfirmationTokenServiceImp confirmationTokenService;
    @InjectMocks
    private CustomerServiceImp customerServiceImp;
    @InjectMocks
    private RegistrationServiceImp registrationServiceImp;

    @BeforeEach
    void setUp() {MockitoAnnotations.openMocks(this);}

    @AfterEach
    void tearDown() {
    }

    @Test
    void save() throws IllegalAccessException {
        /*  //Arrange
        AddressRequestDTO addressRequestDTO = new AddressRequestDTO("street","country","28903");
        CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO(
                "peter",
                "bing",
                "89262293",
                "new@gmail.com",
                "peter",
                "peter123",
                addressRequestDTO
        );
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("peter");
        customer.setLastName("bing");
        customer.setCellphone("89262293");
        customer.setEmail("new@gmail.com");
        customer.setUsername("peter");
        customer.setPwd("peter123");
        when(customerRepository.findCustomerByEmail(any())).thenReturn(Optional.empty())
                .thenReturn(Optional.of(new Customer()));
        when(emailValidator.test(any(String.class))).thenReturn(true);
        when(registrationServiceImp.save(customerRequestDTO)).thenReturn(any(AuthenticationResponseDTO.class));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        //Act
        AuthenticationResponseDTO responseDTO = registrationServiceImp.save(customerRequestDTO);
        //Assertions
        assertNotNull(responseDTO);
        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(emailSender, times(1)).send(any(), any()); // Assuming you have a proper verification for email sending*/
    }

    @Test
    void remove() {
        // Arrange
        Long id = 1L;
        Customer customer = new Customer();
        when(customerRepository.findById(id)).thenReturn(Optional.ofNullable(customer));
        // Act
        boolean result = customerServiceImp.removeCustomer(id);

        // Assertion
        Assertions.assertThat(result).isTrue();
        verify(customerRepository, times(1)).findById(id);
        verify(customerRepository, times(1)).delete(customer);
    }


    @Test
    void editData() {
       //Arrange
        CustomerRequestEditDTO customerRequestEditDTO = new CustomerRequestEditDTO(
                "new peter",
                "new bing",
                "343532",
                "peter@gmail.com",
                "newPeter"
        );
        Customer customerSaved = new Customer();
        customerSaved.setId(1L);
        customerSaved.setName("peter");
        customerSaved.setLastName("bing");
        customerSaved.setCellphone("89262293");
        customerSaved.setEmail("peter@gmail.com");
        customerSaved.setUsername("peter");
        customerSaved.setPwd("peter123");
        Customer customerEdited = new Customer();
        customerEdited.setId(1L);
        customerEdited.setName("new peter");
        customerEdited.setLastName("new bing");
        customerEdited.setCellphone("343532");
        customerEdited.setEmail("peter@gmail.com");
        customerEdited.setUsername("newPeter");
        customerEdited.setPwd("peter123");
        CustomerResponseDTO expectedCustomerResponseDTO = new CustomerResponseDTO(
                1L,
                "new peter",
                "new bing",
                "343532",
                "peter@gmail.com",
                "newPeter"
        );

        when(customerRepository.findById(1L)).thenReturn(Optional.ofNullable(customerSaved));
        when(customerRepository.save(any(Customer.class))).thenReturn(customerEdited);
        when(customerDTOMapper.apply(customerEdited)).thenReturn(expectedCustomerResponseDTO);
        //Act
        CustomerResponseDTO customerResponseDTO = customerServiceImp.editData(customerRequestEditDTO,1L);
        //Assertion
        Assertions.assertThat(customerResponseDTO).isEqualTo(expectedCustomerResponseDTO);
    }

    @Test
    void findById() {
        //Arrange
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("peter");
        customer.setLastName("bing");
        customer.setCellphone("89262293");
        customer.setEmail("peter@gmail.com");
        customer.setUsername("peter");
        customer.setPwd("peter123");
        CustomerResponseDTO expectedCustomerResponseDTO = new CustomerResponseDTO(
                1L,
                "peter",
                "bing",
                "89262293",
                "peter@gmail.com",
                "peter"
        );
        when(customerRepository.findById(1L)).thenReturn(Optional.ofNullable(customer));
        when(customerDTOMapper.apply(customer)).thenReturn(expectedCustomerResponseDTO);
        //Act
        CustomerResponseDTO customerResponseDTO = customerServiceImp.findById(1L);
        //Assertions
        Assertions.assertThat(customerResponseDTO).isEqualTo(expectedCustomerResponseDTO);
        verify(customerRepository,times(1)).findById(1L);
    }

    @Test
    void findAll_whenTheCustomerListIsNotEmpty() {
        //Arrange
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("peter");
        customer1.setLastName("bing");
        customer1.setCellphone("89262293");
        customer1.setEmail("peter@gmail.com");
        customer1.setUsername("peter");
        customer1.setPwd("peter123");
        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("peter2");
        customer2.setLastName("bing2");
        customer2.setCellphone("892622932");
        customer2.setEmail("peter2@gmail.com");
        customer2.setUsername("peter2");
        customer2.setPwd("peter2123");
        CustomerResponseDTO expectedCustomerResponseDTO1 = new CustomerResponseDTO(
                1L,
                "peter",
                "bing",
                "892622932",
                "peter@gmail.com",
                "peter"
        );
        CustomerResponseDTO expectedCustomerResponseDTO2 = new CustomerResponseDTO(
                2L,
                "peter2",
                "bing2",
                "892622932",
                "peter2@gmail.com",
                "peter2"
        );
        List<Customer> customers = Arrays.asList(customer1,customer2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> page = new PageImpl<>(customers, pageable, customers.size());
        when(customerRepository.findAll(pageable)).thenReturn(page);
        when(customerDTOMapper.apply(any(Customer.class)))
                .thenReturn(expectedCustomerResponseDTO1)
                .thenReturn(expectedCustomerResponseDTO2);
        //Act
        List<CustomerResponseDTO> listResponse = customerServiceImp.findAll(0,10);
        //Assertions
        assertNotNull(listResponse);
        assertEquals(2,listResponse.size());
        assertEquals(expectedCustomerResponseDTO1,listResponse.get(0));
        assertEquals(expectedCustomerResponseDTO2,listResponse.get(1));
    }

    @Test
    void findAll_whenTheCustomerListIsEmpty(){
        //Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(customerRepository.findAll(pageable)).thenReturn(emptyPage);
        //Act
        CustomerException exception = assertThrows(CustomerException.class,()->customerServiceImp.findAll(0,10));
        //Assertions
        assertEquals("CUSTOMER_EXCEPTION: The list of customers is null",exception.getMessage());

    }
    @Test
    void findHistory() {
        //Arrange
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("peter");
        customer.setLastName("bing");
        customer.setCellphone("89262293");
        customer.setEmail("peter@gmail.com");
        customer.setUsername("peter");
        customer.setPwd("peter123");
        History history = History.builder()
                .id(1L)
                .sales(null)
                .modificationDate(null)
                .build();
        customer.setHistory(history);
        HistoryResponseDTO expectedHistoryResponseDTO = new HistoryResponseDTO(1L,null,null);
        when(customerRepository.findById(1L)).thenReturn(Optional.ofNullable(customer));
        when(historyDTOMapper.apply(any(History.class))).thenReturn(expectedHistoryResponseDTO);
        //Act
        HistoryResponseDTO historyFound = customerServiceImp.findHistory(1L);
        //Assertion
        Assertions.assertThat(historyFound).isEqualTo(expectedHistoryResponseDTO);
    }

    @Test
    void findAddress() {
        //Arrange
        Address address1 = Address.builder()
                .id(1L)
                .street("street")
                .country("country")
                .postalCode("28903")
                .build();
        Address address2 = Address.builder()
                .id(2L)
                .street("street2")
                .country("country2")
                .postalCode("28902")
                .build();
        List<Address> addresses = Arrays.asList(address1,address2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Address> page = new PageImpl<>(addresses, pageable, addresses.size());
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("peter");
        customer.setLastName("bing");
        customer.setCellphone("89262293");
        customer.setEmail("peter@gmail.com");
        customer.setUsername("peter");
        customer.setPwd("peter123");
        customer.setAddress(addresses);
        AddressResponseDTO responseDTO1 = new AddressResponseDTO(1L,"street","country","28903");
        AddressResponseDTO responseDTO2 = new AddressResponseDTO(2L,"street2","country2","28902");
        List<AddressResponseDTO> expectedAddressResponseDTO = Arrays.asList(responseDTO1,responseDTO2);
        when(customerRepository.findById(1L)).thenReturn(Optional.ofNullable(customer));
        when(customerRepository.findCustomerAddress(1L,pageable)).thenReturn(page);
        when(addressDTOMapper.apply(any(Address.class)))
                .thenReturn(responseDTO1)
                .thenReturn(responseDTO2);
        //Act
        List<AddressResponseDTO> responseDTOS = customerServiceImp.findAddress(1L,0,10);
        //Assertions
        assertNotNull(responseDTOS);
        Assertions.assertThat(responseDTOS).isEqualTo(expectedAddressResponseDTO);
        verify(customerRepository,times(1)).findById(1L);
        verify(customerRepository,times(1)).findCustomerAddress(1L,pageable);


    }

    @Test
    void findCards() {
        CreditCard creditCard1 = new CreditCard(null,"167439276482", TypeCard.MASTER_CARD);
        CreditCard creditCard2 = new CreditCard(null,"1103839276482", TypeCard.VISA);
        List<CreditCard> cards = Arrays.asList(creditCard1,creditCard2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<CreditCard> page = new PageImpl<>(cards, pageable, cards.size());
        CreditCardResponseDTO cardResponseDTO1 = new CreditCardResponseDTO(1L,"167439276482","MASTER_CARD");
        CreditCardResponseDTO cardResponseDTO2 = new CreditCardResponseDTO(2L,"1103839276482","VISA");
        List<CreditCardResponseDTO> expectedResponseDTO = Arrays.asList(cardResponseDTO1,cardResponseDTO2);
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("peter");
        customer.setLastName("bing");
        customer.setCellphone("89262293");
        customer.setEmail("peter@gmail.com");
        customer.setUsername("peter");
        customer.setPwd("peter123");
        customer.setCards(cards);
        when(customerRepository.findById(1L)).thenReturn(Optional.ofNullable(customer));
        when(customerRepository.findCustomerCreditCards(1L,pageable)).thenReturn(page);
        when(creditCardDTOMapper.apply(any(CreditCard.class)))
                .thenReturn(cardResponseDTO1)
                .thenReturn(cardResponseDTO2);
        //Act
        List<CreditCardResponseDTO> list = customerServiceImp.findCards(1L,0,10);
        //Assertion
        assertNotNull(list);
        Assertions.assertThat(list).isEqualTo(expectedResponseDTO);
        verify(customerRepository,times(1)).findById(1L);
        verify(customerRepository,times(1)).findCustomerCreditCards(1L,pageable);

    }

    @Test
    void findByEmail() {
        //Arrange
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("peter");
        customer.setLastName("bing");
        customer.setCellphone("89262293");
        customer.setEmail("peter@gmail.com");
        customer.setUsername("peter");
        customer.setPwd("peter123");
        CustomerResponseDTO expectedCustomerResponseDTO = new CustomerResponseDTO(
                1L,
                "peter",
                "bing",
                "89262293",
                "peter@gmail.com",
                "peter"
        );
        when(customerRepository.findCustomerByEmailAndIsNotDeleted("peter@gmail.com")).thenReturn(Optional.ofNullable(customer));
        when(customerDTOMapper.apply(any(Customer.class))).thenReturn(expectedCustomerResponseDTO);
        //Act
        CustomerResponseDTO customerResponseDTO = customerServiceImp.findByEmail("peter@gmail.com");
        //Assertion
        assertNotNull(customerResponseDTO);
        Assertions.assertThat(customerResponseDTO).isEqualTo(expectedCustomerResponseDTO);
    }
    @Test
    void changeUsername(){
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setUsername("peter");
        ChangeUsernameRequestDTO changeUsernameRequestDTO = new ChangeUsernameRequestDTO(1L,"peter","ertep");
        CustomerResponseDTO expectedCustomerResponseDTO = new CustomerResponseDTO(1L,null,null,null,null,"ertep");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.findCustomerByUsername(changeUsernameRequestDTO.newUsername())).thenReturn(Optional.empty());
        when(customerServiceImp.changeUsername(changeUsernameRequestDTO)).thenReturn(expectedCustomerResponseDTO);

        CustomerResponseDTO customerResponseDTO = customerServiceImp.changeUsername(changeUsernameRequestDTO);
        assertEquals(expectedCustomerResponseDTO.username(),customerResponseDTO.username());
    }
    @Test
    void changeUsername_usernameExist(){
        Customer customerToChangeUsername = new Customer();
        customerToChangeUsername.setId(1L);
        customerToChangeUsername.setUsername("peter");
        Customer customerExisting = new Customer();
        customerExisting.setId(2L);
        customerExisting.setUsername("ertep");
        ChangeUsernameRequestDTO changeUsernameRequestDTO = new ChangeUsernameRequestDTO(1L,"peter","ertep");
        //CustomerResponseDTO expectedCustomerResponseDTO = new CustomerResponseDTO(1L,null,null,null,null,"ertep");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customerToChangeUsername));
        when(customerRepository.findCustomerByUsername(changeUsernameRequestDTO.newUsername())).thenReturn(Optional.of(customerExisting));
       // when(customerServiceImp.changeUsername(changeUsernameRequestDTO)).thenReturn(expectedCustomerResponseDTO);
       CustomerException exception = assertThrows(CustomerException.class,()->customerServiceImp.changeUsername(changeUsernameRequestDTO));
       assertEquals("CUSTOMER_EXCEPTION: The new username exist, therefore is not possible use it",exception.getMessage());
    }
    @Test
    void changeUsername_customerNotExist(){
        ChangeUsernameRequestDTO changeUsernameRequestDTO = new ChangeUsernameRequestDTO(1L,"peter","ertep");
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        CustomerException exception = assertThrows(CustomerException.class,()->customerServiceImp.changeUsername(changeUsernameRequestDTO));
        assertEquals("CUSTOMER_EXCEPTION: The customer fetched to change its username doesn't exist",exception.getMessage());
    }

    @Test
    void changePwd() {
        //Arrange
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("peter");
        customer.setLastName("bing");
        customer.setCellphone("89262293");
        customer.setEmail("peter@gmail.com");
        customer.setUsername("peter");
        customer.setPwd(passwordEncoder.encode("peter123"));

        ChangePwdRequestDTO changePwdRequestDTO = new ChangePwdRequestDTO(1L,"peter123","peter12");
        CustomerResponseDTO expectedResponseDTO = new CustomerResponseDTO(1L,"peter","bing","89262293","peter@gmail.com","peter");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches("peter123", null)).thenReturn(false);
        when(passwordEncoder.matches(changePwdRequestDTO.currentPassword(),customer.getPwd())).thenReturn(true);
        when(customerServiceImp.changePwd(changePwdRequestDTO)).thenReturn(expectedResponseDTO);

        CustomerResponseDTO responseDTO = customerServiceImp.changePwd(changePwdRequestDTO);

        assertNotNull(responseDTO);
    }
    @Test
    void changePwd_passwordNotMatch() {
        //Arrange
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("peter");
        customer.setLastName("bing");
        customer.setCellphone("89262293");
        customer.setEmail("peter@gmail.com");
        customer.setUsername("peter");
        customer.setPwd(passwordEncoder.encode("peter123"));

        ChangePwdRequestDTO changePwdRequestDTO = new ChangePwdRequestDTO(1L,"peter4","peter12");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerException exception = assertThrows(CustomerException.class,()->customerServiceImp.changePwd(changePwdRequestDTO));
        assertEquals("CUSTOMER_EXCEPTION: The passwords doesn't match, therefore is not possible change it",exception.getMessage());
    }
    @Test
    void changePwd_customerNotExist() {
        ChangePwdRequestDTO changePwdRequestDTO = new ChangePwdRequestDTO(1L,"peter123","peter12");

       CustomerException exception = assertThrows(CustomerException.class,()->customerServiceImp.changePwd(changePwdRequestDTO));
       assertEquals("CUSTOMER_EXCEPTION: The customer fetched to change its pwd doesn't exist",exception.getMessage());

    }
    @Test
    void addAddress_addressNotExist_throwException() {
        // Arrange
        AddressResponseDTO newAddressDTO = new AddressResponseDTO(3L, "New Street", "New Country", "12345");
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("peter");
        customer.setLastName("bing");
        customer.setCellphone("89262293");
        customer.setEmail("peter@gmail.com");
        customer.setUsername("peter");
        customer.setPwd("peter123");
        customer.setAddress(new ArrayList<>());
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        // Act
        CustomerException exception = assertThrows(CustomerException.class,()-> customerServiceImp.addAddress(newAddressDTO, 1L));
        // Assert
        assertEquals("CUSTOMER_EXCEPTION: The address to add doesn't exist",exception.getMessage());
    }

    @Test
    void addAddress_addressNotExist() {
        // Arrange
        AddressResponseDTO newAddressDTO = new AddressResponseDTO(null, "New Street", "New Country", "12345");
        Address address = new Address(1L,"New Street", "New Country", "12345");
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("peter");
        customer.setLastName("bing");
        customer.setCellphone("89262293");
        customer.setEmail("peter@gmail.com");
        customer.setUsername("peter");
        customer.setPwd("peter123");
        customer.setAddress(new ArrayList<>());
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.findCustomerAddress(eq(1L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(address)));
        // Act
        List<AddressResponseDTO> result = customerServiceImp.addAddress(newAddressDTO, 1L);
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    @Test
    void addAddress_addressExist(){
        Address address1 = Address.builder()
                .id(1L)
                .street("street")
                .country("country")
                .postalCode("28903")
                .build();
        Address address2 = Address.builder()
                .id(2L)
                .street("street2")
                .country("country2")
                .postalCode("289032")
                .build();
        List<Address> addresses = new ArrayList<>(Arrays.asList(address1));
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("peter");
        customer.setLastName("bing");
        customer.setCellphone("89262293");
        customer.setEmail("peter@gmail.com");
        customer.setUsername("peter");
        customer.setPwd("peter123");
        customer.setAddress(addresses);
        AddressResponseDTO responseDTO1 = new AddressResponseDTO(1L,"street","country","28903");
        AddressResponseDTO responseDTO2 = new AddressResponseDTO(2L,"street2","country2","28902");
        List<AddressResponseDTO> expectedAddressResponseDTO = Arrays.asList(responseDTO1,responseDTO2);
        when(addressRepository.findById(2L)).thenReturn(Optional.ofNullable(address2));
        when(customerRepository.findById(1L)).thenReturn(Optional.ofNullable(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(addressDTOMapper.apply(any(Address.class)))
                .thenReturn(responseDTO1)
                .thenReturn(responseDTO2);
        when(customerRepository.findCustomerAddress(eq(1L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(address1, address2)));

        //Act
        List<AddressResponseDTO> responseDTOS = customerServiceImp.addAddress(responseDTO2,1L);
        //Assertion
        assertNotNull(responseDTOS);
        Assertions.assertThat(responseDTOS).isEqualTo(expectedAddressResponseDTO);
    }
    @Test
    void removeAddress() {
        Address address1 = Address.builder()
                .id(1L)
                .street("street")
                .country("country")
                .postalCode("28903")
                .build();
        List<Address> addresses = new ArrayList<>(Arrays.asList(address1));
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("peter");
        customer.setLastName("bing");
        customer.setCellphone("89262293");
        customer.setEmail("peter@gmail.com");
        customer.setUsername("peter");
        customer.setPwd("peter123");
        customer.setAddress(addresses);
        when(customerRepository.findById(1L)).thenReturn(Optional.ofNullable(customer));
        when(addressRepository.findById(1L)).thenReturn(Optional.ofNullable(address1));
        //Act
        boolean result = customerServiceImp.removeAddress(1L,1L);
        //Assertions
        assertTrue(result);
        assertFalse(customer.getAddress().contains(address1));
        verify(customerRepository,times(1)).findById(1L);
        verify(addressRepository,times(1)).findById(1L);
    }

    @Test
    //TODO:fix this test method
    void addCreditCard_whenCardNotExist() {
        //Arrange
        CreditCard card = CreditCard.builder()
                .id(1L)
                .number("12345")
                .typeCard(TypeCard.MASTER_CARD)
                .build();
        List<CreditCard> cards = new ArrayList<>(Arrays.asList(card));
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("peter");
        customer.setLastName("bing");
        customer.setCellphone("89262293");
        customer.setEmail("peter@gmail.com");
        customer.setUsername("peter");
        customer.setPwd("peter123");
        customer.setCards(new ArrayList<>());
        CreditCardResponseDTO expectedCardDTO1 = new CreditCardResponseDTO(1L,"7890","VISA");
        CreditCardResponseDTO cardDTOToAdd = new CreditCardResponseDTO(null,"7890","VISA");
        List<CreditCardResponseDTO> expectedList = new ArrayList<>(Arrays.asList(expectedCardDTO1));
        //when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerRepository.findById(1L)).thenReturn(Optional.ofNullable(customer));
        when(customerRepository.findCustomerCreditCards(1L,PageRequest.of(0,10))).thenReturn(any(Page.class));
        when(customerServiceImp.addCreditCard(cardDTOToAdd,1L)).thenReturn(expectedList);
        when(creditCardDTOMapper.apply(any(CreditCard.class)))
                .thenReturn(expectedCardDTO1);
        //Act
        List<CreditCardResponseDTO> list = customerServiceImp.addCreditCard(cardDTOToAdd,1L);
        //Assertion
        assertNotNull(list);
        Assertions.assertThat(list).isEqualTo(expectedList);

    }
    @Test
    //TODO:fix this test method
    void addCreditCard_whenCardExit() {
        //Arrange
        CreditCard card = CreditCard.builder()
                .id(1L)
                .number("12345")
                .typeCard(TypeCard.MASTER_CARD)
                .build();
        CreditCard card2 = CreditCard.builder()
                .id(2L)
                .number("78902")
                .typeCard(TypeCard.VISA)
                .build();
        List<CreditCard> cards = new ArrayList<>(Arrays.asList(card));
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("peter");
        customer.setLastName("bing");
        customer.setCellphone("89262293");
        customer.setEmail("peter@gmail.com");
        customer.setUsername("peter");
        customer.setPwd("peter123");
        customer.setCards(cards);
        CreditCardResponseDTO expectedCardDTO1 = new CreditCardResponseDTO(1L,"12345","MASTER_CARD");
        CreditCardResponseDTO expectedCardDTO2 = new CreditCardResponseDTO(2L,"78902","VISA");
        List<CreditCardResponseDTO> expectedList = new ArrayList<>(Arrays.asList(expectedCardDTO1,expectedCardDTO2));
        when(creditCardRepository.findById(2L)).thenReturn(Optional.ofNullable(card2));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerRepository.findById(1L)).thenReturn(Optional.ofNullable(customer));
        when(creditCardDTOMapper.apply(any(CreditCard.class)))
                .thenReturn(expectedCardDTO1)
                .thenReturn(expectedCardDTO2);
        //Act
        List<CreditCardResponseDTO> list = customerServiceImp.addCreditCard(expectedCardDTO2,1L);
        //Assertion
        assertNotNull(list);
        Assertions.assertThat(list).isEqualTo(expectedList);

    }

    @Test
    void removeCreditCard() {
        //Arrange
        CreditCard card = CreditCard.builder()
                .id(1L)
                .number("12345")
                .typeCard(TypeCard.MASTER_CARD)
                .build();
        List<CreditCard> cards = new ArrayList<>(Arrays.asList(card));
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("peter");
        customer.setLastName("bing");
        customer.setCellphone("89262293");
        customer.setEmail("peter@gmail.com");
        customer.setUsername("peter");
        customer.setPwd("peter123");
        customer.setCards(cards);
        when(customerRepository.findById(1L)).thenReturn(Optional.ofNullable(customer));
        when(creditCardRepository.findById(1L)).thenReturn(Optional.ofNullable(card));
        //Act
        boolean result = customerServiceImp.removeCreditCard(1l,1L);
        //Assertions
        assertTrue(result);
        assertFalse(customer.getCards().contains(card));
        verify(customerRepository,times(1)).findById(1L);
        verify(creditCardRepository,times(1)).findById(1L);
    }
}