package com.ccor.ecommerce.repository;

import com.ccor.ecommerce.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    @Query("SELECT p.customer FROM Payment p WHERE p.id=:id")
    Optional<Customer> findCustomer(@Param("id") Long id);
    @Query("SELECT p.card FROM Payment p WHERE p.id=:id")
    Optional<CreditCard> findCard(@Param("id")Long id);
    @Query("SELECT p.sale FROM Payment p WHERE p.id=:id")
    Optional<Sale> findSale(@Param("id")Long id);
    @Query("SELECT p FROM Payment p WHERE p.statusPayment=:status")
    Page<Payment> findPaymentSalesByStatus(@Param("status")StatusPayment statusPayment, Pageable pageable);
}
