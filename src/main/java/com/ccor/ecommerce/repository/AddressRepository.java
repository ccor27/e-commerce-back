package com.ccor.ecommerce.repository;

import com.ccor.ecommerce.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {

    List<Address> findAddressesByPostalCode(String postalCode);
    List<Address> findAddressesByCountry(String country);

}
