package com.ccor.ecommerce.repository;

import com.ccor.ecommerce.model.ProductSold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSoldRepository extends JpaRepository<ProductSold,Long> {
    List<ProductSold> findProductsSoldByBarCode(String barcode);
}
