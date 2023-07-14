package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.ProductSold;
import com.ccor.ecommerce.model.dto.ProductSoldRequestDTO;
import com.ccor.ecommerce.model.dto.ProductSoldResponseDTO;
import com.ccor.ecommerce.repository.ProductSoldRepository;
import com.ccor.ecommerce.service.mapper.ProductSoldDTOMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductSoldServiceImpTest {
    @Mock
    private ProductSoldRepository productSoldRepository;
    @Mock
    private ProductSoldDTOMapper productSoldDTOMapper;
    @InjectMocks
    private ProductSoldServiceImp productSoldServiceImp;

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
        ProductSold productSold = ProductSold.builder()
                .id(1L)
                .barCode("1as2")
                .name("product")
                .amount(10)
                .price(10.0)
                .build();
        ProductSoldRequestDTO productSoldRequestDTO = new ProductSoldRequestDTO("1as2","product",10,10.0);
        ProductSoldResponseDTO expectedProductSoldResponseDTO = new ProductSoldResponseDTO(1L,"1as2","product",10,10.0);
        when(productSoldRepository.save(any(ProductSold.class))).thenReturn(productSold);
        when(productSoldDTOMapper.apply(any(ProductSold.class))).thenReturn(expectedProductSoldResponseDTO);
        //Act
        ProductSoldResponseDTO productSoldResponseDTO = productSoldServiceImp.save(productSoldRequestDTO);
        //Assertion
        assertNotNull(productSoldResponseDTO);
        Assertions.assertThat(productSoldResponseDTO).isEqualTo(expectedProductSoldResponseDTO);
    }

    @Test
    void edit() {
        //Arrange
        ProductSold productSold1 = ProductSold.builder()
                .id(1L)
                .barCode("1as2")
                .name("product")
                .amount(10)
                .price(10.0)
                .build();
        ProductSoldRequestDTO productSoldRequestDTO = new ProductSoldRequestDTO("1as2New","productNew",10,10.0);
        ProductSoldResponseDTO expectedProductSoldResponseDTO = new ProductSoldResponseDTO(1L,"1as2New","productNew",10,10.0);
        when(productSoldRepository.save(any(ProductSold.class))).thenReturn(productSold1);
        when(productSoldRepository.findById(1L)).thenReturn(Optional.ofNullable(productSold1));
        when(productSoldDTOMapper.apply(any(ProductSold.class))).thenReturn(expectedProductSoldResponseDTO);
        //Act
        ProductSoldResponseDTO productSoldResponseDTO = productSoldServiceImp.edit(productSoldRequestDTO,1L);
        assertNotNull(productSoldResponseDTO);
        Assertions.assertThat(productSoldResponseDTO).isEqualTo(expectedProductSoldResponseDTO);
    }

    @Test
    void remove() {
        //Arrange
        when(productSoldRepository.existsById(1L)).thenReturn(true);
        //Act
        boolean result = productSoldServiceImp.remove(1L);
        //Assertion
        assertTrue(result);

    }

    @Test
    void findById() {
        //Arrange
        ProductSold productSold1 = ProductSold.builder()
                .id(1L)
                .barCode("1as2")
                .name("product")
                .amount(10)
                .price(10.0)
                .build();
        ProductSoldResponseDTO expectedProductSoldResponseDTO = new ProductSoldResponseDTO(1L,"1as2","product",10,10.0);
        when(productSoldRepository.findById(1L)).thenReturn(Optional.ofNullable(productSold1));
        when(productSoldDTOMapper.apply(any(ProductSold.class))).thenReturn(expectedProductSoldResponseDTO);
        //Act
        ProductSoldResponseDTO productSoldResponseDTO = productSoldServiceImp.findById(1L);
        //Assertion
        assertNotNull(productSoldResponseDTO);
        Assertions.assertThat(productSoldResponseDTO).isEqualTo(expectedProductSoldResponseDTO);
    }

    @Test
    void findAll() {
        //Arrange
        ProductSold productSold1 = ProductSold.builder()
                .id(1L)
                .barCode("1as2")
                .name("product")
                .amount(10)
                .price(10.0)
                .build();
        List<ProductSold> list = new ArrayList<>(Arrays.asList(productSold1));
        ProductSoldResponseDTO expectedProductSoldResponseDTO = new ProductSoldResponseDTO(1L,"1as2","product",10,10.0);
        List<ProductSoldResponseDTO> expectedListProductSold = new ArrayList<>(Arrays.asList(expectedProductSoldResponseDTO));
        when(productSoldRepository.findAll()).thenReturn(list);
        when(productSoldDTOMapper.apply(any(ProductSold.class))).thenReturn(expectedProductSoldResponseDTO);
        //Act
        List<ProductSoldResponseDTO> productSoldResponseDTOS = productSoldServiceImp.findAll();
        //Assertion
        assertNotNull(productSoldResponseDTOS);
        Assertions.assertThat(productSoldResponseDTOS).isEqualTo(expectedListProductSold);
    }

    @Test
    void findProductsSoldByBarCode() {
        //Arrange
        ProductSold productSold1 = ProductSold.builder()
                .id(1L)
                .barCode("1as2")
                .name("product")
                .amount(10)
                .price(10.0)
                .build();
        List<ProductSold> list = new ArrayList<>(Arrays.asList(productSold1));
        ProductSoldResponseDTO expectedProductSoldResponseDTO = new ProductSoldResponseDTO(1L,"1as2","product",10,10.0);
        List<ProductSoldResponseDTO> expectedListProductSold = new ArrayList<>(Arrays.asList(expectedProductSoldResponseDTO));
        when(productSoldRepository.findProductsSoldByBarCode("1as2")).thenReturn(list);
        when(productSoldDTOMapper.apply(any(ProductSold.class))).thenReturn(expectedProductSoldResponseDTO);
        //Act
        List<ProductSoldResponseDTO> productSoldResponseDTOS = productSoldServiceImp.findProductsSoldByBarCode("1as2");
        //Assertion
        assertNotNull(productSoldResponseDTOS);
        Assertions.assertThat(productSoldResponseDTOS).isEqualTo(expectedListProductSold);
    }
}