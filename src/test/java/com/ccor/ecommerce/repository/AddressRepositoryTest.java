package com.ccor.ecommerce.repository;

import com.ccor.ecommerce.model.Address;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class AddressRepositoryTest {
    @Autowired
    private AddressRepository addressRepository;

    @BeforeEach
    void setUp() {
        Address address1 = new Address(null,"spain avenue","Spain","28903");
        Address address2 = new Address(null,"valencia street","Spain","28903");
        Address address3 = new Address(null,"margaritas","Spain","28903");
        Address address4 = new Address(null,"angeles avenue","France","28904");
        Address address5 = new Address(null,"plaza spain","France","28904");
        addressRepository.save(address1);
        addressRepository.save(address2);
        addressRepository.save(address3);
        addressRepository.save(address4);
        addressRepository.save(address5);
    }

    @AfterEach
    void tearDown() {
        addressRepository.deleteAll();
    }

    @Test
    void findAddressesByPostalCode() {
        Page<Address> address = addressRepository.findAddressesByPostalCode(PageRequest.of(0,10),"28903");
        assertEquals(address.getTotalElements(),3);
    }

    @Test
    void findAddressesByCountry() {
        Page<Address> addresses = addressRepository.findAddressesByCountry(PageRequest.of(0,10),"France");
        assertEquals(addresses.getTotalElements(),2);
    }
}