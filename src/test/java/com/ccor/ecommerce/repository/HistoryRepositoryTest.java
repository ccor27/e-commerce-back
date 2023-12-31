package com.ccor.ecommerce.repository;

import com.ccor.ecommerce.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class HistoryRepositoryTest extends Person {

    @Autowired
    private HistoryRepository historyRepository;

    @BeforeEach
    void setUp() {

        ProductSold productSold1 = new ProductSold(null,"AHD26493874728","Computer",10,20000);
        ProductSold productSold2 = new ProductSold(null,"SDC26493801728","Tv",15,15000);
        Sale sale = new Sale();
        sale.setConcept("Things to home");
        sale.setProductsSold(Arrays.asList(productSold1,productSold2));
        sale.setCreateAt(new Date());
        History history = History.builder()
                .sales(Arrays.asList(sale))
                .modificationDate(new Date())
                .build();
        history.setModificationDate(new Date());
        historyRepository.save(history);
    }

    @AfterEach
    void tearDown() {
        historyRepository.deleteAll();
    }

    @Test
    void findHistorySales() {
        Page<Sale> sales = historyRepository.findHistorySales(1L, PageRequest.of(0,10));
        assertTrue(sales!=null);
    }
}