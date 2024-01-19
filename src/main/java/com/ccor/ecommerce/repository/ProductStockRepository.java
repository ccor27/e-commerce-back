package com.ccor.ecommerce.repository;

import com.ccor.ecommerce.model.ProductStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock,Long> {
    @Query("SELECT p FROM ProductStock p WHERE p.enableProduct=true")
    Page<ProductStock> findProductStocksByEnableProduct(Pageable pageable);
    @Query("SELECT p FROM ProductStock p WHERE p.barCode=:barcode AND p.enableProduct=true")
    Optional<ProductStock> findProductStockByBarCode(@Param("barcode") String barcode);
    boolean existsByName(String name);
    boolean existsByBarCode(String barcode);
    @Query("SELECT COUNT(p) FROM ProductStock p WHERE p.enableProduct=true")
    int countByEnableProduct();
    @Query("SELECT p FROM ProductStock  p WHERE p.barCode=:barCode AND p.enableProduct=true AND p.amount>=:amountToSell")
    Optional<ProductStock> amountIsAvailable(@Param("barCode") String barCode,@Param("amountToSell") int amountToSell);
    @Query("SELECT COUNT(*) FROM ProductStock p WHERE p.enableProduct=true ")
    int countProductStock();
}
