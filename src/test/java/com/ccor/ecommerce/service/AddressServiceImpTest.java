package com.ccor.ecommerce.service;

import com.ccor.ecommerce.exceptions.AddressException;
import com.ccor.ecommerce.model.Address;
import com.ccor.ecommerce.model.dto.AddressRequestDTO;
import com.ccor.ecommerce.model.dto.AddressResponseDTO;
import com.ccor.ecommerce.repository.AddressRepository;
import com.ccor.ecommerce.service.mapper.AddressDTOMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.NotNull;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceImpTest {
    @InjectMocks
    private AddressServiceImp addressServiceImp;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private AddressDTOMapper addressDTOMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void save() {
        //Arrange
        AddressRequestDTO requestDTO = new AddressRequestDTO("street","country","28903");
        Address savedAddress = Address.builder()
                .id(1L)
                .street("street")
                .country("country")
                .postalCode("28903")
                .build();
        AddressResponseDTO expectedResponseDTO = new AddressResponseDTO(1L,"street","country","28903");
        when(addressRepository.save(any(Address.class))).thenReturn(savedAddress);
        when(addressDTOMapper.apply(savedAddress)).thenReturn(expectedResponseDTO);
        //Act
        AddressResponseDTO responseDTO = addressServiceImp.save(requestDTO);
        Assertions.assertThat(responseDTO).isEqualTo(expectedResponseDTO);
    }

    @Test
    void edit() {
        Long id =1L;
    AddressRequestDTO requestDTO = new AddressRequestDTO("new street","new country","28903");
        Address existAddress = Address.builder()
                .id(id)
                .street("street")
                .country("country")
                .postalCode("28903")
                .build();
        Address updatedAddress = Address.builder()
                .id(id)
                .street("new street")
                .country("new country")
                .postalCode("28903")
                .build();
        AddressResponseDTO expectedAddressDTO = new AddressResponseDTO(1L,"new street","new country","28903");
        when(addressRepository.findById(id)).thenReturn(Optional.ofNullable(existAddress));
        when(addressRepository.save(any(Address.class))).thenReturn(updatedAddress);
        when(addressDTOMapper.apply(updatedAddress)).thenReturn(expectedAddressDTO);
        AddressResponseDTO actualAddressDTO = addressServiceImp.edit(requestDTO,1L);
        Assertions.assertThat(actualAddressDTO).isEqualTo(expectedAddressDTO);
    }

    @Test
    void remove_ExistingId_ReturnsTrue() {
        //Arrange
        Long id = 1L;
        when(addressRepository.existsById(id)).thenReturn(true);
        //Act
        boolean result = addressServiceImp.remove(id);
        //Assert
        Assertions.assertThat(result).isTrue();
        verify(addressRepository,times(1)).deleteById(id);
    }
    @Test
    void remove_ExistingId_ReturnsFalse() {
        //Arrange
        Long id = 1L;
        //Act
        //Assert
        AddressException exception = assertThrows(AddressException.class,()-> addressServiceImp.remove(id));
        assertEquals("ADDRESS_EXCEPTION: The address to delete doesn't exist",exception.getMessage());
    }
    @Test
    void findById() {
        //Arrange
        Long id=1L;
        Address existAddress = Address.builder()
                .id(id)
                .street("street")
                .country("country")
                .postalCode("28903")
                .build();
        AddressResponseDTO expectedResponseDTO = new AddressResponseDTO(1L,"street","country","28903");
        when(addressRepository.findById(id)).thenReturn(Optional.ofNullable(existAddress));
        when(addressDTOMapper.apply(existAddress)).thenReturn(expectedResponseDTO);
        //Act
        AddressResponseDTO responseDTO = addressServiceImp.findById(id);
        //Assert
        Assertions.assertThat(responseDTO).isEqualTo(expectedResponseDTO);
    }

    @Test
    void findAll_whenTheAddressListNotEmpty() {
        // Arrange
        Address address1 = Address.builder().id(1L).street("street1").country("country1").postalCode("28903").build();
        Address address2 = Address.builder().id(2L).street("street2").country("country2").postalCode("28903").build();
        List<Address> addresses = Arrays.asList(address1, address2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Address> page = new PageImpl<>(addresses, pageable, addresses.size());
        when(addressRepository.findAll(pageable)).thenReturn(page);
        when(addressDTOMapper.apply(any(Address.class)))
                .thenReturn(new AddressResponseDTO(1L, "street1", "country1", "28903"))
                .thenReturn(new AddressResponseDTO(2L, "street2", "country2", "28903"));
        // Act
        List<AddressResponseDTO> result = addressServiceImp.findAll(0, 10);
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }
    @Test
    void findAll_whenTheAddressListEmpty() {
        // Arrange
        when(addressRepository.findAll(any(Pageable.class))).thenReturn(null);

        // Act and Assert
        AddressException exception = assertThrows(AddressException.class, () -> addressServiceImp.findAll(0, 10));
        assertEquals("ADDRESS_EXCEPTION: The list of addresses fetched is null",exception.getMessage());


    }

    @Test
    void findAddressesByPostalCode() {
        //Arrange
        Address address1 = Address.builder().id(1L).street("street1").country("country1").postalCode("28903").build();
        Address address2 = Address.builder().id(2L).street("street2").country("country2").postalCode("28903").build();
        List<Address> addresses = Arrays.asList(address1, address2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Address> page = new PageImpl<>(addresses, pageable, addresses.size());
        when(addressRepository.findAddressesByPostalCode(PageRequest.of(0,10),"28903")).thenReturn(page);
        //Act
        AddressResponseDTO expectedResponseDTO1 = new AddressResponseDTO(1L,"street1","country1","28903");
        AddressResponseDTO expectedResponseDTO2 = new AddressResponseDTO(2L,"street2","country2","28903");
        when(addressDTOMapper.apply(any(Address.class)))
                .thenReturn(expectedResponseDTO1)
                .thenReturn(expectedResponseDTO2);
        List<AddressResponseDTO> expectedList = addressServiceImp.findAddressesByPostalCode(0,10,"28903");
        //Assert
        assertEquals(2,expectedList.size());
        verify(addressRepository,times(1)).findAddressesByPostalCode(PageRequest.of(0,10),"28903");
        verify(addressDTOMapper,times(2)).apply(any(Address.class));


    }

    @Test
    void findAddressesByCountry() {
        //Arrange
        Address address1 = Address.builder().id(1L).street("street1").country("country").postalCode("28903").build();
        Address address2 = Address.builder().id(2L).street("street2").country("country").postalCode("28903").build();
        List<Address> addresses = Arrays.asList(address1, address2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Address> page = new PageImpl<>(addresses, pageable, addresses.size());
        when(addressRepository.findAddressesByCountry(PageRequest.of(0,10),"country")).thenReturn((page));
        //Act
        AddressResponseDTO expectedResponseDTO1 = new AddressResponseDTO(1L,"street1","country","28903");
        AddressResponseDTO expectedResponseDTO2 = new AddressResponseDTO(2L,"street2","country","28903");
        when(addressDTOMapper.apply(any(Address.class)))
                .thenReturn(expectedResponseDTO1)
                .thenReturn(expectedResponseDTO2);
        List<AddressResponseDTO> expectedList = addressServiceImp.findAddressesByCountry(0,10,"country");
        //Assert
        assertEquals(2,expectedList.size());
        verify(addressRepository,times(1)).findAddressesByCountry(PageRequest.of(0,10),"country");
        verify(addressDTOMapper,times(2)).apply(any(Address.class));

    }
}