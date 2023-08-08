package com.ccor.ecommerce.repository;

import com.ccor.ecommerce.model.ProductSold;
import com.ccor.ecommerce.model.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale,Long> {
    @Query("SELECT s.productsSold FROM Sale s WHERE s.id= :saleId")
    Page<ProductSold> findSaleProductsSold(@Param("saleId")Long id, Pageable pageable);
    List<Sale> findSalesByCreateAt(Date date);
}
