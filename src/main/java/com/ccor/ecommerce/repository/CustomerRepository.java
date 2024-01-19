package com.ccor.ecommerce.repository;

import com.ccor.ecommerce.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    @Query("SELECT c.history FROM Customer c WHERE c.id= :customerId")
    Optional<History> findCustomerHistory(@Param("customerId")Long id);
    @Query("SELECT c.address FROM Customer c WHERE c.id= :customerId")
    Page<Address> findCustomerAddress(@Param("customerId")Long id, Pageable pageable);
    @Query("SELECT c.cards FROM Customer c WHERE c.id= :customerId")
    Page<CreditCard> findCustomerCreditCards(@Param("customerId")Long id,Pageable pageable);
    @Query("SELECT c FROM Customer c WHERE c.isDeleted = false")
    Page<Customer> findAllNotDeleted(Pageable pageable);
    @Query("SELECT c FROM Customer c WHERE c.isDeleted=false AND c.receiveNotifications=true")
    List<Customer> findAllNotDeletedAndCanReceiveNotifications();
    @Query("SELECT c FROM Customer c WHERE c.email= :email AND c.isDeleted=false")
    Optional<Customer> findCustomerByEmailAndIsNotDeleted(@Param("email") String email);
    Optional<Customer> findCustomerByUsername(String username);
    @Query("SELECT c FROM Customer c WHERE c.id= :id AND c.isDeleted=false")
    Optional<Customer> findCustomerByIdAndIsNotDeleted(@Param("id") Long id);
    @Query("SELECT c FROM Customer c WHERE c.username= :username AND c.isDeleted=false")
    Optional<Customer> findCustomerByUsernameAndIsNotDeleted(@Param("username")String username);
    @Query("SELECT DISTINCT c FROM Customer c JOIN c.address a WHERE a.id = :addressId AND c.isDeleted = false")
    List<Customer> findCustomersByAddressId(@Param("addressId") Long addressId);
    @Query("SELECT DISTINCT c FROM Customer c JOIN c.cards ca WHERE ca.id = :addressId AND c.isDeleted = false")
    List<Customer> findCustomersByCardId(@Param("addressId") Long cardId);
    @Query("SELECT COUNT(a) FROM Customer c JOIN c.address a WHERE c.id=:id AND c.isDeleted=false")
    int countByAddressByCustomerAndIsDeletedFalse(@Param("id") Long id);
    @Query("SELECT COUNT(cd) FROM Customer c JOIN c.cards cd WHERE c.id=:id AND c.isDeleted=false")
    int countByCreditCardsByCustomerAndIsDeletedFalse(@Param("id")Long id);
    @Query("SELECT c.history.sales FROM Customer c WHERE c.id=:id AND c.isDeleted=false")
    List<Sale> findCustomerSales(@Param("id") Long id);
    @Query("SELECT p FROM Customer c JOIN c.history h JOIN h.sales s JOIN s.payment p WHERE c.id = :id AND c.isDeleted = false")
    List<Payment> findCustomerPayments(@Param("id") Long id);

}
