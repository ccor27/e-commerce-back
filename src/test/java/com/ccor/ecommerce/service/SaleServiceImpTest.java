package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.ProductSold;
import com.ccor.ecommerce.model.Sale;
import com.ccor.ecommerce.model.dto.ProductSoldRequestDTO;
import com.ccor.ecommerce.model.dto.ProductSoldResponseDTO;
import com.ccor.ecommerce.model.dto.SaleRequestDTO;
import com.ccor.ecommerce.model.dto.SaleResponseDTO;
import com.ccor.ecommerce.repository.ProductSoldRepository;
import com.ccor.ecommerce.repository.SaleRepository;
import com.ccor.ecommerce.service.mapper.ProductSoldDTOMapper;
import com.ccor.ecommerce.service.mapper.SaleDTOMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceImpTest {
    @Mock
    private SaleRepository saleRepository;
    @Mock
    private ProductSoldRepository productSoldRepository;
    @Mock
    private SaleDTOMapper saleDTOMapper;
    @Mock
    private ProductSoldDTOMapper productSoldDTOMapper;
    @InjectMocks
    private SaleServiceImp saleServiceImp;

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
        Sale sale = Sale.builder()
                .id(1L)
                .concept("concept")
                .productsSold(Collections.emptyList())
                .createAt(new Date(2023,07,14))
                .build();
        SaleResponseDTO expectedSaleResponseDTO = new SaleResponseDTO(1L,"concept",Collections.emptyList(),new Date(2023,07,14));
        SaleRequestDTO saleRequestDTO = new SaleRequestDTO("concept",Collections.emptyList(),new Date(2023,07,14));
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);
        when(saleDTOMapper.apply(any(Sale.class))).thenReturn(expectedSaleResponseDTO);
        //Act
        SaleResponseDTO saleResponseDTO = saleServiceImp.save(saleRequestDTO);
        //Assertion
        assertNotNull(saleResponseDTO);
        Assertions.assertThat(saleResponseDTO).isEqualTo(expectedSaleResponseDTO);
    }

    @Test
    void edit() {
        //Arrange
        Sale sale = Sale.builder()
                .id(1L)
                .concept("concept")
                .productsSold(Collections.emptyList())
                .createAt(new Date(2023,07,14))
                .build();
        SaleResponseDTO expectedSaleResponseDTO = new SaleResponseDTO(1L,"conceptEdited",Collections.emptyList(),new Date(2023,07,14));
        SaleRequestDTO saleRequestDTO = new SaleRequestDTO("conceptEdited",Collections.emptyList(),new Date(2023,07,14));
        when(saleRepository.findById(1L)).thenReturn(Optional.ofNullable(sale));
        when(saleDTOMapper.apply(any(Sale.class))).thenReturn(expectedSaleResponseDTO);
        //Act
        SaleResponseDTO saleResponseDTO = saleServiceImp.edit(saleRequestDTO,1L);
        //Assertion
        assertNotNull(saleResponseDTO);
        Assertions.assertThat(saleResponseDTO).isEqualTo(expectedSaleResponseDTO);
        Assertions.assertThat(saleResponseDTO.concept()).isEqualToIgnoringCase("conceptEdited");
    }

    @Test
    void findById() {
        //Arrange
        Sale sale = Sale.builder()
                .id(1L)
                .concept("concept")
                .productsSold(Collections.emptyList())
                .createAt(new Date(2023,07,14))
                .build();
        SaleResponseDTO expectedSaleResponseDTO = new SaleResponseDTO(1L,"concept",Collections.emptyList(),new Date(2023,07,14));
        when(saleRepository.findById(1L)).thenReturn(Optional.ofNullable(sale));
        when(saleRepository.existsById(1L)).thenReturn(true);
        when(saleDTOMapper.apply(any(Sale.class))).thenReturn(expectedSaleResponseDTO);
        //Act
        SaleResponseDTO saleResponseDTO = saleServiceImp.findById(1L);
        //Assertions
        assertNotNull(saleResponseDTO);
        Assertions.assertThat(saleResponseDTO).isEqualTo(expectedSaleResponseDTO);
        verify(saleRepository,times(1)).existsById(1L);
        verify(saleRepository,times(1)).findById(1L);
        verify(saleDTOMapper,times(1)).apply(any(Sale.class));
    }

    @Test
    void findAll() {
        //Arrange
        Sale sale = Sale.builder()
                .id(1L)
                .concept("concept")
                .productsSold(Collections.emptyList())
                .createAt(new Date(2023,07,14))
                .build();
        List<Sale> sales = new ArrayList<>(Arrays.asList(sale));
        SaleResponseDTO expectedSaleResponseDTO = new SaleResponseDTO(1L,"concept",Collections.emptyList(),new Date(2023,07,14));
        List<SaleResponseDTO> expectedList = new ArrayList<>(Arrays.asList(expectedSaleResponseDTO));
        when(saleRepository.findAll()).thenReturn(sales);
        when(saleDTOMapper.apply(any(Sale.class))).thenReturn(expectedSaleResponseDTO);
        //Act
        List<SaleResponseDTO> responseDTOS = saleServiceImp.findAll();
        //Assertions
        assertNotNull(responseDTOS);
        Assertions.assertThat(responseDTOS).isEqualTo(expectedList);

    }

    @Test
    void addProductSold() {
        //Arrange
        ProductSoldRequestDTO productSoldRequestDTO = new ProductSoldRequestDTO("1as2","product",10,50.0);
        ProductSoldResponseDTO productSoldResponseDTO = new ProductSoldResponseDTO(1L,"1as2","product",10,50.0);
        Sale sale = Sale.builder()
                .id(1L)
                .concept("concept")
                .productsSold(new ArrayList<>())
                .createAt(new Date(2023,07,14))
                .build();
        SaleResponseDTO expectedSaleResponseDTO = new SaleResponseDTO(1L,"concept",Arrays.asList(productSoldResponseDTO),new Date(2023,07,14));
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);
        when(saleRepository.findById(1L)).thenReturn(Optional.ofNullable(sale));
        when(saleDTOMapper.apply(any(Sale.class))).thenReturn(expectedSaleResponseDTO);
        //Act
        SaleResponseDTO saleResponseDTO = saleServiceImp.addProductSold(productSoldRequestDTO,1L);
        //Assertions
        assertNotNull(saleResponseDTO);
        Assertions.assertThat(saleResponseDTO).isEqualTo(expectedSaleResponseDTO);
        Assertions.assertThat(saleResponseDTO.products().size()).isEqualTo(1);

    }

    @Test
    void removeProductSold() {
        //Arrange
        ProductSold productSold = ProductSold.builder()
                .id(1L)
                .barCode("1as2")
                .name("product")
                .amount(10)
                .price(50.0)
                .build();
        ProductSoldResponseDTO productSoldResponseDTO = new ProductSoldResponseDTO(1L,"1as2","product",10,50.0);
        Sale sale = Sale.builder()
                .id(1L)
                .concept("concept")
                .productsSold(new ArrayList<>(Arrays.asList(productSold)))
                .createAt(new Date(2023,07,14))
                .build();
        SaleResponseDTO expectedSaleResponseDTO = new SaleResponseDTO(1L,"concept",Collections.emptyList(),new Date(2023,07,14));
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);
       when(saleRepository.findById(1L)).thenReturn(Optional.ofNullable(sale));
       when(productSoldRepository.findById(1L)).thenReturn(Optional.ofNullable(productSold));
       when(saleDTOMapper.apply(any(Sale.class))).thenReturn(expectedSaleResponseDTO);
       //Act
       SaleResponseDTO saleResponseDTO = saleServiceImp.removeProductSold(productSoldResponseDTO,1L);
       //Assertion
        assertNotNull(saleResponseDTO);
        Assertions.assertThat(saleResponseDTO).isEqualTo(expectedSaleResponseDTO);
        Assertions.assertThat(saleResponseDTO.products().size()).isEqualTo(0);
    }

    @Test
    void findProductsSold() {
        //Arrange
        ProductSold productSold = ProductSold.builder()
                .id(1L)
                .barCode("1as2")
                .name("product")
                .amount(10)
                .price(50.0)
                .build();
        List<ProductSold> productSolds = new ArrayList<>(Arrays.asList(productSold));
        ProductSoldResponseDTO productSoldResponseDTO = new ProductSoldResponseDTO(1L,"1as2","product",10,50.0);
        List<ProductSoldResponseDTO> expectedList = new ArrayList<>(Arrays.asList(productSoldResponseDTO));
        Sale sale = Sale.builder()
                .id(1L)
                .concept("concept")
                .productsSold(productSolds)
                .createAt(new Date(2023,07,14))
                .build();
        when(saleRepository.existsById(1L)).thenReturn(true);
        when(saleRepository.findSaleProductsSold(1L)).thenReturn(productSolds);
        when(productSoldDTOMapper.apply(any(ProductSold.class))).thenReturn(productSoldResponseDTO);
        //Act
        List<ProductSoldResponseDTO> productSoldResponseDTOS = saleServiceImp.findProductsSold(1L);
        //Assertions
        assertNotNull(productSoldResponseDTOS);
        Assertions.assertThat(productSoldResponseDTOS).isEqualTo(expectedList);
        Assertions.assertThat(productSoldResponseDTOS.size()).isEqualTo(1);



    }
}