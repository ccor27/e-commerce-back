package com.ccor.ecommerce.repository;

import com.ccor.ecommerce.model.ProductSold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductSoldRepository extends JpaRepository<ProductSold,Long> {
    List<ProductSold> findProductSoldsByBarCode(String barcode);
}
