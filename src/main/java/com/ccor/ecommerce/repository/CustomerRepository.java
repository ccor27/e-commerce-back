package com.ccor.ecommerce.repository;

import com.ccor.ecommerce.model.Address;
import com.ccor.ecommerce.model.CreditCard;
import com.ccor.ecommerce.model.Customer;
import com.ccor.ecommerce.model.History;
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
    List<Address> findCustomerAddress(@Param("customerId")Long id);
    @Query("SELECT c.cards FROM Customer c WHERE c.id= :customerId")
    List<CreditCard> findCustomerCreditCards(@Param("customerId")Long id);
    Optional<Customer> findCustomerByEmail(String email);
    Optional<Customer> findCustomerByUsername(String username);
}
