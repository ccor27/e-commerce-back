package com.ccor.ecommerce.repository;

import com.ccor.ecommerce.model.Payment;
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
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale,Long> {
    @Query("SELECT s.productsSold FROM Sale s WHERE s.id= :saleId")
    Page<ProductSold> findSaleProductsSold(@Param("saleId")Long id, Pageable pageable);
    @Query("SELECT s.payment FROM Sale s WHERE s.id=:id")
    Optional<Payment> findPaymentSale(@Param("id")Long id);
    @Query("SELECT COUNT(products) FROM Sale s JOIN s.productsSold products WHERE s.id=:id")
    int countProductsSold(@Param("id") Long id);
    @Query("SELECT s FROM Sale s WHERE s.createAt = :date")
    List<Sale> findSalesByDate(@Param("date") Date date,  Pageable pageable);
    @Query("SELECT COUNT(s) FROM Sale s WHERE s.createAt=:date")
    int countSalesByDate(@Param("date") Date date);
    @Query("SELECT COUNT(s) FROM Sale s WHERE s.createAt BETWEEN :startDate AND :endDate")
    int countSalesByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    @Query("SELECT s FROM Sale s WHERE s.createAt BETWEEN :startDate AND :endDate")
    List<Sale> findSalesByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable pageable);
    @Query("SELECT COUNT(s) FROM Sale s ")
    int countSales();
    @Query("SELECT s FROM Sale s WHERE s.payment.id=:id AND s.payment.isDeleted=false")
    Sale findSaleByPayment(@Param("id") Long id);

}
