package com.ccor.ecommerce.repository;

import com.ccor.ecommerce.model.CanceledSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CanceledSaleRepository extends JpaRepository<CanceledSale,Long> {
    @Query("SELECT COUNT(*) FROM CanceledSale ")
    int countCanceledSales();
}
