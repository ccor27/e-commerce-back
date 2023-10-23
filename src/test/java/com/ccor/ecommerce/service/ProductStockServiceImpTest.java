package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.ProductSold;
import com.ccor.ecommerce.model.ProductStock;
import com.ccor.ecommerce.model.dto.ProductStockRequestDTO;
import com.ccor.ecommerce.model.dto.ProductStockResponseDTO;
import com.ccor.ecommerce.repository.ProductStockRepository;
import com.ccor.ecommerce.service.mapper.ProductStockDTOMapper;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductStockServiceImpTest {
    @Mock
    private ProductStockRepository productStockRepository;
    @Mock
    private ProductStockDTOMapper productStockDTOMapper;
    @InjectMocks
    private ProductStockServiceImp productStockServiceImp;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void save() {
        ProductStock productStock = ProductStock.builder()
                .id(1L)
                .name("product")
                .amount(10)
                .pricePerUnit(10.0)
                .barCode("1as2")
                .enableProduct(true)
                .build();
        ProductStockRequestDTO productStockRequestDTO = new ProductStockRequestDTO("product",10,10.0,"1as2",true);
        ProductStockResponseDTO expectedProductStockResponseDTO = new ProductStockResponseDTO(1l,"product",10,10.0,"1as2",true);
        when(productStockRepository.save(any(ProductStock.class))).thenReturn(productStock);
        when(productStockDTOMapper.apply(any(ProductStock.class))).thenReturn(expectedProductStockResponseDTO);
        //Act
        ProductStockResponseDTO productStockResponseDTO = productStockServiceImp.save(productStockRequestDTO);
        //Assertion
        assertNotNull(productStockResponseDTO);
        Assertions.assertThat(productStockResponseDTO).isEqualTo(expectedProductStockResponseDTO);
    }

    @Test
    void remove() {
        //Arrange
        Long id = 1L;
        when(productStockRepository.existsById(id)).thenReturn(true);
        //Act
        boolean result = productStockServiceImp.remove(id);
        //Assertion
        assertTrue(result);
    }

    @Test
    void edit() {
        ProductStock productStock = ProductStock.builder()
                .id(1L)
                .name("product")
                .amount(10)
                .pricePerUnit(10.0)
                .barCode("1as2")
                .enableProduct(true)
                .build();
        ProductStock productStockEdited = ProductStock.builder()
                .id(1L)
                .name("productEdit")
                .amount(10)
                .pricePerUnit(10.0)
                .barCode("1as2")
                .enableProduct(true)
                .build();
        ProductStockRequestDTO productStockRequestDTO = new ProductStockRequestDTO("productEdit",10,10.0,"1as2",true);
        ProductStockResponseDTO expectedProductStockResponseDTO = new ProductStockResponseDTO(1l,"productEdit",10,10.0,"1as2",true);
        when(productStockRepository.save(any(ProductStock.class))).thenReturn(productStock);
        when(productStockRepository.findById(1L)).thenReturn(Optional.ofNullable(productStockEdited));
        when(productStockDTOMapper.apply(any(ProductStock.class))).thenReturn(expectedProductStockResponseDTO);
        //Act
        ProductStockResponseDTO productStockResponseDTO = productStockServiceImp.edit(productStockRequestDTO,1L);
        //Assertion
        assertNotNull(productStockResponseDTO);
        Assertions.assertThat(productStockResponseDTO).isEqualTo(expectedProductStockResponseDTO);
    }

    @Test
    void findProductStocksByEnableProduct() {
        ProductStock productStock = ProductStock.builder()
                .id(1L)
                .name("product")
                .amount(10)
                .pricePerUnit(10.0)
                .barCode("1as2")
                .enableProduct(true)
                .build();
        List<ProductStock> productStocks = new ArrayList<>(Arrays.asList(productStock));
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductStock> page = new PageImpl<>(productStocks, pageable, productStocks.size());
        ProductStockResponseDTO expectedProductStockResponseDTO = new ProductStockResponseDTO(1l,"product",10,10.0,"1as2",true);
        List<ProductStockResponseDTO> expectedList = new ArrayList<>(Arrays.asList(expectedProductStockResponseDTO));
        when(productStockRepository.findProductStocksByEnableProduct(pageable)).thenReturn(page);
        when(productStockDTOMapper.apply(any(ProductStock.class))).thenReturn(expectedProductStockResponseDTO);
        //Act
        List<ProductStockResponseDTO> listProductsDTOS = productStockServiceImp.findProductStocksByEnableProduct(0,10);
        //Assertion
        assertNotNull(listProductsDTOS);
        Assertions.assertThat(listProductsDTOS).isEqualTo(expectedList);
    }

    @Test
    void findProductStocksByBarCode() {
        ProductStock productStock = ProductStock.builder()
                .id(1L)
                .name("product")
                .amount(10)
                .pricePerUnit(10.0)
                .barCode("1as2")
                .enableProduct(true)
                .build();
        ProductStockResponseDTO expectedProductStockResponseDTO = new ProductStockResponseDTO(1l,"product",10,10.0,"1as2",true);
        when(productStockRepository.findProductStockByBarCode("1as2")).thenReturn(Optional.ofNullable(productStock));
        when(productStockDTOMapper.apply(any(ProductStock.class))).thenReturn(expectedProductStockResponseDTO);
        //Act
        ProductStockResponseDTO productDTOS = productStockServiceImp.findProductStockByBarCode("1as2");
        //Assertion
        assertNotNull(expectedProductStockResponseDTO);
        Assertions.assertThat(productDTOS).isEqualTo(expectedProductStockResponseDTO);
    }

    @Test
    void sellProduct() {
        ProductStock productStock = ProductStock.builder()
                .id(1L)
                .name("product")
                .amount(10)
                .pricePerUnit(10.0)
                .barCode("1as2")
                .enableProduct(true)
                .build();
        ProductStockResponseDTO expectedProductStockResponseDTO = new ProductStockResponseDTO(1l,"product",8,10.0,"1as2",true);
        when(productStockRepository.save(any(ProductStock.class))).thenReturn(productStock);
        when(productStockRepository.findById(1L)).thenReturn(Optional.ofNullable(productStock));
        when(productStockDTOMapper.apply(any(ProductStock.class))).thenReturn(expectedProductStockResponseDTO);
        //Act
        ProductStockResponseDTO productStockResponseDTO = productStockServiceImp.sellProduct(2,1L);
        //Assertion
        assertNotNull(productStockResponseDTO);
        Assertions.assertThat(productStockResponseDTO.amount()).isEqualTo(8);
        Assertions.assertThat(productStockResponseDTO).isEqualTo(expectedProductStockResponseDTO);
    }
}