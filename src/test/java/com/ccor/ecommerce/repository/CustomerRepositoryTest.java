package com.ccor.ecommerce.repository;

import com.ccor.ecommerce.model.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private  CustomerRepository customerRepository;

    @BeforeAll
      void setUp() {
        History history = new History(null,null,new ArrayList<>(),new Date());
        Address address = new Address(null,"spain avenue","Spain","28903");
        List<Address> addresses = new ArrayList<>();
        addresses.add(address);
        CreditCard creditCard1 = new CreditCard(null,"167439276482", TypeCard.MASTER_CARD);
        CreditCard creditCard2 = new CreditCard(null,"1103839276482", TypeCard.VISA);
        List<CreditCard> cards = new ArrayList<>();
        cards.add(creditCard1);
        cards.add(creditCard2);

        Customer customer = new Customer(
                history,
                addresses,
                cards,
                new ArrayList<Token>(),
                new ArrayList<ConfirmationToken>(),
                true,
                Arrays.asList(Role.CUSTOMER));
        customer.setName("peter");
        customer.setLastName("bing");
        customer.setCellphone("89262293");
        customer.setEmail("peter@gmail.com");
        customer.setUsername("peter");
        customer.setPwd("peter123");
        customerRepository.save(customer);
 }


    @AfterAll
     void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    void findCustomerHistory() {
        assertTrue(customerRepository.findCustomerHistory(1L).isPresent());
    }

    @Test
    void findCustomerAddress() {
        assertTrue(customerRepository.findCustomerAddress(1L).isPresent());
    }

    @Test
    void findCustomerCreditCards() {
        assertTrue(!customerRepository.findCustomerCreditCards(1L).isEmpty());
        assertTrue(customerRepository.findCustomerCreditCards(4L).isEmpty());
    }

    @Test
    void findCustomerByEmail() {
        assertTrue(customerRepository.findCustomerByEmail("peter@gmail.com").isPresent());
        assertFalse(customerRepository.findCustomerByEmail("petr@gmail.com").isPresent());
    }

    @Test
    void findCustomerById() {
        assertTrue(customerRepository.findCustomerById(1L).isPresent());
        assertFalse(customerRepository.findCustomerById(2L).isPresent());
    }
}