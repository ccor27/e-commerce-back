package com.ccor.ecommerce.repository;

import com.ccor.ecommerce.model.CreditCard;
import com.ccor.ecommerce.model.TypeCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard,Long> {
    @Query("SELECT c FROM CreditCard c WHERE c.typeCard =:typeCard")
    Page<CreditCard> findCreditCardsByTypeCard(Pageable pageable, @Param("typeCard") TypeCard typeCard);
    Optional<CreditCard> findCreditCardByNumber(String number);
    @Query("SELECT COUNT(*) FROM CreditCard")
    int countCreditCards();

}
