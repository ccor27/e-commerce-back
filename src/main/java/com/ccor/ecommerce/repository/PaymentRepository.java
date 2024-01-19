package com.ccor.ecommerce.repository;

import com.ccor.ecommerce.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    @Query("SELECT p.card FROM Payment p WHERE p.id=:id")
    Optional<CreditCard> findCard(@Param("id")Long id);
    @Query("SELECT p FROM Payment p WHERE p.statusPayment=:status AND p.isDeleted=false")
    Page<Payment> findPaymentsByStatusAndEnable(@Param("status")StatusPayment statusPayment, Pageable pageable);
    @Query("SELECT p FROM Payment p WHERE p.statusPayment=:status")
    Page<Payment> findPaymentsByStatus(@Param("status")StatusPayment statusPayment, Pageable pageable);
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.statusPayment=:status AND p.isDeleted=false")
    int countPaymentsByStatusPaymentAndEnable(@Param("status") StatusPayment status);
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.statusPayment=:status")
    int countAllPaymentsByStatusPayment(@Param("status") StatusPayment status);
    @Query("SELECT COUNT(p) FROM Payment p")
    int countAllPayments();
    Optional<Payment> findPaymentByIdStripePayment(String idStripePayment);
}
