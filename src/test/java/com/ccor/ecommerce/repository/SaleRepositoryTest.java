package com.ccor.ecommerce.repository;

import com.ccor.ecommerce.model.Person;
import com.ccor.ecommerce.model.ProductSold;
import com.ccor.ecommerce.model.Sale;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
class SaleRepositoryTest extends Person {
    @Autowired
    private SaleRepository saleRepository;

    @BeforeAll
    void setUp() {
        ProductSold productSold1 = new ProductSold(null,"AHD26493874728","Computer",10,20000);
        ProductSold productSold2 = new ProductSold(null,"SDC26493801728","Tv",15,15000);
        Sale sale1 = new Sale(null,"Things to home", Arrays.asList(productSold1,productSold2),new Date(2023,06,3));


        ProductSold productSold3 = new ProductSold(null,"LKNS6493874728","HMD",8,9000);
        ProductSold productSold4 = new ProductSold(null,"SDC26000172828","Led light",8,8000);
        Sale sale2 = new Sale(null,"Things to home", Arrays.asList(productSold3,productSold4),new Date(2023,06,3));

        ProductSold productSold5 = new ProductSold(null,"SDC26493801728","Tv",9,15000);
        ProductSold productSold6 = new ProductSold(null,"SDC26493801728","Tv",1,15000);
        Sale sale3 = new Sale(null,"Things to home", Arrays.asList(productSold5,productSold6),new Date(2023,06,3));


        ProductSold productSold7 = new ProductSold(null,"SDC26493801728","Tv",2,15000);
        ProductSold productSold8 = new ProductSold(null,"SDC26493801728","Tv",3,15000);
        Sale sale4 = new Sale(null,"Things to home", Arrays.asList(productSold7,productSold8),new Date(2023,04,12));

        saleRepository.save(sale1);
        saleRepository.save(sale2);
        saleRepository.save(sale3);
        saleRepository.save(sale4);
    }

    @AfterAll
    void tearDown() {
      saleRepository.deleteAll();
    }

    @Test
    void findSaleProductsSold() {
        List<ProductSold> solds1 = saleRepository.findSaleProductsSold(1L);
        assertEquals(solds1.size(),2);
        List<ProductSold> solds2 = saleRepository.findSaleProductsSold(10L);
        assertTrue(solds2.isEmpty() || solds2==null);
    }

    @Test
    void findSalesByCreateAt() {
    }
}