package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.*;
import com.ccor.ecommerce.model.dto.*;
import com.ccor.ecommerce.repository.CreditCardRepository;
import com.ccor.ecommerce.repository.CustomerRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CreditCardRepository creditCardRepository;
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
                .payment(new Payment())
                .build();
        CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO(1L,"","","","","");
        CreditCardResponseDTO cardResponseDTO = new CreditCardResponseDTO(1L,"","");
        PaymentRequestDTO requestDTO = new PaymentRequestDTO(StatusPayment.UNPAID.name(),null,customerResponseDTO,cardResponseDTO);
        SaleResponseDTO expectedSaleResponseDTO = new SaleResponseDTO(1L,"concept",Collections.emptyList(),new Date(2023,07,14),1L);
        SaleRequestDTO saleRequestDTO = new SaleRequestDTO("concept",Collections.emptyList(),new Date(2023,07,14),requestDTO);
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
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO("",new Date(),null,null);
        SaleResponseDTO expectedSaleResponseDTO = new SaleResponseDTO(1L,"conceptEdited",Collections.emptyList(),new Date(2023,07,14),1L);
        SaleRequestDTO saleRequestDTO = new SaleRequestDTO("conceptEdited",Collections.emptyList(),new Date(2023,07,14),paymentRequestDTO);
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
                .payment(new Payment())
                .build();
        SaleResponseDTO expectedSaleResponseDTO = new SaleResponseDTO(1L,"concept",Collections.emptyList(),new Date(2023,07,14),1L);
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
                .payment(new Payment())
                .build();
        List<Sale> sales = new ArrayList<>(Arrays.asList(sale));
        Pageable pageable = PageRequest.of(0, 10);
        Page<Sale> page = new PageImpl<>(sales, pageable, sales.size());
        SaleResponseDTO expectedSaleResponseDTO = new SaleResponseDTO(1L,"concept",Collections.emptyList(),new Date(2023,07,14),1L);
        List<SaleResponseDTO> expectedList = new ArrayList<>(Arrays.asList(expectedSaleResponseDTO));
        when(saleRepository.findAll(pageable)).thenReturn(page);
        when(saleDTOMapper.apply(any(Sale.class))).thenReturn(expectedSaleResponseDTO);
        //Act
        List<SaleResponseDTO> responseDTOS = saleServiceImp.findAll(0,10);
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
                .payment(new Payment())
                .build();
        SaleResponseDTO expectedSaleResponseDTO = new SaleResponseDTO(1L,"concept",Arrays.asList(productSoldResponseDTO),new Date(2023,07,14),1L);
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
                .payment(new Payment())
                .build();
        SaleResponseDTO expectedSaleResponseDTO = new SaleResponseDTO(1L,"concept",Collections.emptyList(),new Date(2023,07,14),1L);
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);
       when(saleRepository.findById(1L)).thenReturn(Optional.ofNullable(sale));
       when(productSoldRepository.findById(1L)).thenReturn(Optional.ofNullable(productSold));
       when(saleDTOMapper.apply(any(Sale.class))).thenReturn(expectedSaleResponseDTO);
       //Act
       SaleResponseDTO saleResponseDTO = saleServiceImp.removeProductSold(productSoldResponseDTO.id(),1L);
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
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductSold> page = new PageImpl<>(productSolds, pageable, productSolds.size());
        ProductSoldResponseDTO productSoldResponseDTO = new ProductSoldResponseDTO(1L,"1as2","product",10,50.0);
        List<ProductSoldResponseDTO> expectedList = new ArrayList<>(Arrays.asList(productSoldResponseDTO));
        Sale sale = Sale.builder()
                .id(1L)
                .concept("concept")
                .productsSold(productSolds)
                .createAt(new Date(2023,07,14))
                .build();
        when(saleRepository.existsById(1L)).thenReturn(true);
        when(saleRepository.findSaleProductsSold(1L,pageable)).thenReturn(page);
        when(productSoldDTOMapper.apply(any(ProductSold.class))).thenReturn(productSoldResponseDTO);
        //Act
        List<ProductSoldResponseDTO> productSoldResponseDTOS = saleServiceImp.findProductsSold(1L,0,10);
        //Assertions
        assertNotNull(productSoldResponseDTOS);
        Assertions.assertThat(productSoldResponseDTOS).isEqualTo(expectedList);
        Assertions.assertThat(productSoldResponseDTOS.size()).isEqualTo(1);



    }
}