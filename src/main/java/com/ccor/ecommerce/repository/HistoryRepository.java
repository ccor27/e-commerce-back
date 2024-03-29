package com.ccor.ecommerce.repository;

import com.ccor.ecommerce.model.History;
import com.ccor.ecommerce.model.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History,Long> {
    @Query("SELECT h.sales FROM History h WHERE h.id= :historyId")
    Page<Sale> findHistorySales(@Param("historyId")Long id, Pageable pageable);
    @Query("SELECT COUNT(*) FROM History ")
    int countHistory();
    
}
