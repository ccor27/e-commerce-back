package com.ccor.ecommerce.repository;

import com.ccor.ecommerce.model.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock,Long> {
    @Query("SELECT p FROM ProductStock p WHERE p.enableProduct=true")
    List<ProductStock> findProductStocksByEnableProduct();
    Optional<ProductStock> findProductStockByBarCode(String barcode);
}
