package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.History;
import com.ccor.ecommerce.model.Payment;
import com.ccor.ecommerce.model.Sale;
import com.ccor.ecommerce.model.dto.HistoryRequestDTO;
import com.ccor.ecommerce.model.dto.HistoryResponseDTO;
import com.ccor.ecommerce.model.dto.SaleResponseDTO;
import com.ccor.ecommerce.repository.HistoryRepository;
import com.ccor.ecommerce.repository.SaleRepository;
import com.ccor.ecommerce.service.mapper.HistoryDTOMapper;
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
class HistoryServiceImpTest {
    @Mock
    private HistoryRepository historyRepository;
    @Mock
    private SaleRepository saleRepository;
    @Mock
    private HistoryDTOMapper historyDTOMapper;
    @Mock
    private SaleDTOMapper saleDTOMapper;
    @InjectMocks
    private HistoryServiceImp historyServiceImp;

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
        History history = new History(1L,new ArrayList<>(),new Date(2023,07,14));
        HistoryRequestDTO historyRequestDTO = new HistoryRequestDTO(new Date(2023,07,14));
        HistoryResponseDTO expectedHistoryResponseDTO = new HistoryResponseDTO(1L, Collections.emptyList(),new Date(2023,07,14));
        when(historyRepository.save(any(History.class))).thenReturn(history);
        when(historyDTOMapper.apply(any(History.class))).thenReturn(expectedHistoryResponseDTO);
        //Act
        HistoryResponseDTO historyResponseDTO = historyServiceImp.save(historyRequestDTO);
        //Assertion
        assertNotNull(historyResponseDTO);
        Assertions.assertThat(historyResponseDTO).isEqualTo(expectedHistoryResponseDTO);

    }

    @Test
    void remove() {
        //Arrange
        History history = new History(1L,new ArrayList<>(),null);
        when(historyRepository.existsById(1L)).thenReturn(true);
        //Act
        boolean result = historyServiceImp.remove(1L);
        //Assertion
        assertTrue(result);
    }

    @Test
    void findById() {
        //Arrange
        History history = new History(1L,new ArrayList<>(),new Date(2023,07,14));
        HistoryResponseDTO expectedHistoryResponseDTO = new HistoryResponseDTO(1L, Collections.emptyList(),new Date(2023,07,14));
        when(historyRepository.findById(1L)).thenReturn(Optional.ofNullable(history));
        when(historyDTOMapper.apply(any(History.class))).thenReturn(expectedHistoryResponseDTO);
        //Act
        HistoryResponseDTO historyResponseDTO = historyServiceImp.findById(1L);
        //Assertions
        assertNotNull(historyResponseDTO);
        Assertions.assertThat(historyResponseDTO).isEqualTo(expectedHistoryResponseDTO);
    }

    @Test
    void findAll() {
        //Arrange
        History history1 = new History(1L,new ArrayList<>(),new Date(2023,07,14));
        History history2 = new History(1L,new ArrayList<>(),new Date(2023,07,14));
        List<History> expectedListHistory= new ArrayList<>(Arrays.asList(history1,history2));
        Pageable pageable = PageRequest.of(0, 10);
        Page<History> page = new PageImpl<>(expectedListHistory, pageable, expectedListHistory.size());
        HistoryResponseDTO expectedHistoryResponseDTO1 = new HistoryResponseDTO(1L, Collections.emptyList(),new Date(2023,07,14));
        HistoryResponseDTO expectedHistoryResponseDTO2 = new HistoryResponseDTO(2L, Collections.emptyList(),new Date(2023,07,14));
        List<HistoryResponseDTO> expectedListHistoryResponseDTO = new ArrayList<>(Arrays.asList(expectedHistoryResponseDTO1,expectedHistoryResponseDTO2));
        when(historyRepository.findAll(pageable)).thenReturn(page);
        when(historyDTOMapper.apply(any(History.class)))
                .thenReturn(expectedHistoryResponseDTO1)
                .thenReturn(expectedHistoryResponseDTO2);
        //Act
        List<HistoryResponseDTO> historyResponseDTOS = historyServiceImp.findAll(0,10);
        //Assertion
        assertNotNull(historyResponseDTOS);
        Assertions.assertThat(historyResponseDTOS).isEqualTo(expectedListHistoryResponseDTO);
    }

    @Test
    void findSales() {
        //Arrange
        Sale sale1 = new Sale(1L,"concept",Collections.emptyList(),new Date(2023,07,14),new Payment());
        Sale sale2 = new Sale(2L,"concept",Collections.emptyList(),new Date(2023,07,14),new Payment());
        List<Sale> sales = new ArrayList<>(Arrays.asList(sale1,sale2));
        Pageable pageable = PageRequest.of(0, 10);
        Page<Sale> page = new PageImpl<>(sales, pageable, sales.size());
        SaleResponseDTO expectedSaleResponseDTO1 = new SaleResponseDTO(1L,"concept",Collections.emptyList(),new Date(2023,07,14),1L);
        SaleResponseDTO expectedSaleResponseDTO2 = new SaleResponseDTO(2L,"concept",Collections.emptyList(),new Date(2023,07,14),2L);
        List<SaleResponseDTO> expectedSalesResponseDTOS = new ArrayList<>(Arrays.asList(expectedSaleResponseDTO1,expectedSaleResponseDTO2));
        History history = History.builder()
                .id(1L)
                .sales(sales)
                .modificationDate(new Date(2023,07,14))
                .build();
//        when(historyRepository.save(any(History.class))).thenReturn(history);
        when(historyRepository.findHistorySales(1L, pageable)).thenReturn(page);
        when(saleDTOMapper.apply(any(Sale.class)))
                .thenReturn(expectedSaleResponseDTO1)
                .thenReturn(expectedSaleResponseDTO2);
        //Act
        List<SaleResponseDTO> responseDTOS = historyServiceImp.findSales(1L,0,10);
        //Assertions
        assertNotNull(responseDTOS);
        Assertions.assertThat(responseDTOS).isEqualTo(expectedSalesResponseDTOS);
    }

    @Test
    void addSale() {
        //Arrange
        Sale sale2 = new Sale(2L,"concept",Collections.emptyList(),new Date(2023,07,14),new Payment());
        List<Sale> sales = new ArrayList<>(Arrays.asList(sale2));
        Pageable pageable = PageRequest.of(0, 10);
        Page<Sale> page = new PageImpl<>(sales, pageable, sales.size());
        SaleResponseDTO expectedSaleResponseDTO2 = new SaleResponseDTO(2L,"concept",Collections.emptyList(),new Date(2023,07,14),2L);
        List<SaleResponseDTO> expectedListSaleResponseDTO = new ArrayList<>(Arrays.asList(expectedSaleResponseDTO2));
        History history = History.builder()
                .id(1L)
                .sales(new ArrayList<>())
                .modificationDate(new Date(2023,07,14))
                .build();
        when(historyRepository.save(any(History.class))).thenReturn(history);
        when(saleRepository.findById(2L)).thenReturn(Optional.ofNullable(sale2));
        when(historyRepository.findById(1L)).thenReturn(Optional.ofNullable(history));
        when(historyRepository.findHistorySales(anyLong(), any(Pageable.class))).thenReturn(page);

        when(saleDTOMapper.apply(any(Sale.class)))
                .thenReturn(expectedSaleResponseDTO2);
        //Act
        List<SaleResponseDTO> responseDTOS = historyServiceImp.addExistingSale(expectedSaleResponseDTO2,1L);
        //Assertions
        assertNotNull(responseDTOS);
        Assertions.assertThat(responseDTOS).isEqualTo(expectedListSaleResponseDTO);
    }

    @Test
    void removeSale() {
        //Arrange
        Sale sale1 = new Sale(1L,"concept",Collections.emptyList(),new Date(2023,07,14),new Payment());
        List<Sale> sales = new ArrayList<>(Arrays.asList(sale1));
        History history = History.builder()
                .id(1L)
                .sales(sales)
                .modificationDate(new Date(2023,07,14))
                .build();
        when(historyRepository.findById(1L)).thenReturn(Optional.ofNullable(history));
        when(saleRepository.findById(1L)).thenReturn(Optional.ofNullable(sale1));
        //Act
        boolean result = historyServiceImp.removeSale(1L,1L);
        assertTrue(result);
        assertFalse(sales.contains(sale1));
        verify(historyRepository,times(1)).findById(1L);
        verify(saleRepository,times(1)).findById(1L);
    }
}