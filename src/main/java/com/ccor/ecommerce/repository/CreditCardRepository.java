package com.ccor.ecommerce.repository;

import com.ccor.ecommerce.model.CreditCard;
import com.ccor.ecommerce.model.TypeCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard,Long> {
    List<CreditCard> findCreditCardsByTypeCard(TypeCard typeCard);
    Optional<CreditCard> findCreditCardByNumber(String number);
}
