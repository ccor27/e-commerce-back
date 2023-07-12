package com.ccor.ecommerce.repository;

import com.ccor.ecommerce.model.Person;
import com.ccor.ecommerce.model.ProductSold;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class ProductSoldRepositoryTest extends Person {
    @Autowired
    private ProductSoldRepository productSoldRepository;
    @BeforeEach
    void setUp() {
        ProductSold productSold1 = new ProductSold(null,"AHD26493874728","Computer",10,20000);
        ProductSold productSold2 = new ProductSold(null,"SDC26493801728","Tv",15,15000);
        ProductSold productSold3 = new ProductSold(null,"LKNS6493874728","HMD",8,9000);
        ProductSold productSold4 = new ProductSold(null,"SDC26000172828","Led light",8,8000);
        ProductSold productSold5 = new ProductSold(null,"SDC26493801728","Tv",9,15000);
        ProductSold productSold6 = new ProductSold(null,"SDC26493801728","Tv",1,15000);
        ProductSold productSold7 = new ProductSold(null,"SDC26493801728","Tv",2,15000);
        productSoldRepository.save(productSold1);
        productSoldRepository.save(productSold2);
        productSoldRepository.save(productSold3);
        productSoldRepository.save(productSold4);
        productSoldRepository.save(productSold5);
        productSoldRepository.save(productSold6);
        productSoldRepository.save(productSold7);
    }

    @AfterEach
    void tearDown() {
        productSoldRepository.deleteAll();
    }

    @Test
    void findProductSoldsByBarCode() {
        List<ProductSold> solds1 = productSoldRepository.findProductsSoldByBarCode("SDC26493801728");
        assertTrue(!solds1.isEmpty() || solds1!=null);
        List<ProductSold> solds2 = productSoldRepository.findProductsSoldByBarCode("SDC2649380178");
        assertFalse(solds1.isEmpty() || solds1==null);
    }
}