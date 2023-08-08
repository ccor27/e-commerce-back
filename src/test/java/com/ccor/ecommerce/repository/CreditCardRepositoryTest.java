package com.ccor.ecommerce.repository;

import com.ccor.ecommerce.model.CreditCard;
import com.ccor.ecommerce.model.TypeCard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class CreditCardRepositoryTest {
    @Autowired
    private CreditCardRepository creditCardRepository;

    @BeforeEach
    void setUp() {
        CreditCard creditCard1 = new CreditCard(null,"167439276482", TypeCard.MASTER_CARD);
        CreditCard creditCard2 = new CreditCard(null,"1103839276482", TypeCard.VISA);
        CreditCard creditCard3 = new CreditCard(null,"167439280182", TypeCard.AMERICAN_EXPRESS);
        CreditCard creditCard4 = new CreditCard(null,"167439178382", TypeCard.VISA);
        CreditCard creditCard5 = new CreditCard(null,"167401016482", TypeCard.AMERICAN_EXPRESS);
        creditCardRepository.save(creditCard1);
        creditCardRepository.save(creditCard2);
        creditCardRepository.save(creditCard3);
        creditCardRepository.save(creditCard4);
        creditCardRepository.save(creditCard5);
    }

    @AfterEach
    void tearDown() {
        creditCardRepository.deleteAll();
    }

    @Test
    void findCreditCardsByTypeCard() {
        Page<CreditCard> cards = creditCardRepository.findCreditCardsByTypeCard(PageRequest.of(0,10),TypeCard.MASTER_CARD);
        assertEquals(cards.getTotalElements(),1);
    }

    @Test
    void findCreditCardByNumber() {
        CreditCard card1 = creditCardRepository.findCreditCardByNumber("167439276482").orElse(null);
        boolean t=false;
        if(card1!=null)
            t=true;
        assertTrue(t);
        CreditCard card2 = creditCardRepository.findCreditCardByNumber("16743927648").orElse(null);
        t=false;
        if(card2!=null)
            t=true;
        assertFalse(t);
    }
}