package com.ccor.ecommerce.service;

import com.ccor.ecommerce.exceptions.CreditCardException;
import com.ccor.ecommerce.model.Address;
import com.ccor.ecommerce.model.CreditCard;
import com.ccor.ecommerce.model.TypeCard;
import com.ccor.ecommerce.model.dto.CreditCardRequestDTO;
import com.ccor.ecommerce.model.dto.CreditCardResponseDTO;
import com.ccor.ecommerce.repository.CreditCardRepository;
import com.ccor.ecommerce.service.mapper.CreditCardDTOMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditCardServiceImpTest {
    @Mock
    private CreditCardRepository creditCardRepository;
    @Mock
    private CreditCardDTOMapper creditCardDTOMapper;
    @InjectMocks
    private CreditCardServiceImp creditCardServiceImp;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this);    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void save() {
        //Arrange
        CreditCard cardSaved = CreditCard.builder()
                .id(1L)
                .number("1234")
                .typeCard(TypeCard.MASTER_CARD)
                .build();
        CreditCardResponseDTO expectedCreditCardResponseDTO = new CreditCardResponseDTO(1L,"1234","MASTER_CARD");
        CreditCardRequestDTO requestDTO = new CreditCardRequestDTO("1234","MASTER_CARD");
        when(creditCardRepository.save(any(CreditCard.class))).thenReturn(cardSaved);
        when(creditCardDTOMapper.apply(cardSaved)).thenReturn(expectedCreditCardResponseDTO);
        //Act
        CreditCardResponseDTO responseDTO = creditCardServiceImp.save(requestDTO);
        //Assertion
        Assertions.assertThat(responseDTO).isEqualTo(expectedCreditCardResponseDTO);
    }

    @Test
    void edit() {
        //Arrange

        CreditCardRequestDTO edit = new CreditCardRequestDTO("5678","VISA");
        CreditCard cardSaved = CreditCard.builder()
                .id(1L)
                .number("1234")
                .typeCard(TypeCard.MASTER_CARD)
                .build();
        CreditCard updatedCard = CreditCard.builder()
                .id(1L)
                .number("5678")
                .typeCard(TypeCard.VISA)
                .build();
        CreditCardResponseDTO expectedCreditCardResponseDTO = new CreditCardResponseDTO(1L,"5678","VISA");
        when(creditCardRepository.findById(1L)).thenReturn(Optional.ofNullable(cardSaved));
        when(creditCardRepository.save(any(CreditCard.class))).thenReturn(updatedCard);
        when(creditCardDTOMapper.apply(updatedCard)).thenReturn(expectedCreditCardResponseDTO);
        //Act
        CreditCardResponseDTO cardResponseDTO = creditCardServiceImp.edit(edit,1L);
        //Assertion
        Assertions.assertThat(cardResponseDTO).isEqualTo(expectedCreditCardResponseDTO);

    }

    @Test
    void findById() {
        //Arrange
        CreditCard cardSaved = CreditCard.builder()
                .id(1L)
                .number("1234")
                .typeCard(TypeCard.MASTER_CARD)
                .build();
        CreditCardResponseDTO expectedCreditCardResponseDTO = new CreditCardResponseDTO(1L,"1234","MASTER_CARD");
        when(creditCardRepository.findById(1L)).thenReturn(Optional.ofNullable(cardSaved));
        when(creditCardDTOMapper.apply(cardSaved)).thenReturn(expectedCreditCardResponseDTO);
        //Act
        CreditCardResponseDTO responseDTO = creditCardServiceImp.findById(1L);
        //Assertion
        Assertions.assertThat(responseDTO).isEqualTo(expectedCreditCardResponseDTO);
    }

    @Test
    void findCardByNumber() {
        //Arrange
        CreditCard cardSaved = CreditCard.builder()
                .id(1L)
                .number("1234")
                .typeCard(TypeCard.MASTER_CARD)
                .build();
        CreditCardResponseDTO expectedCreditCardResponseDTO = new CreditCardResponseDTO(1L,"1234","MASTER_CARD");
        when(creditCardRepository.findCreditCardByNumber("1234")).thenReturn(Optional.ofNullable(cardSaved));
        when(creditCardDTOMapper.apply(cardSaved)).thenReturn(expectedCreditCardResponseDTO);
        //Act
        CreditCardResponseDTO responseDTO = creditCardServiceImp.findCardByNumber("1234");
        //Assertion
        Assertions.assertThat(responseDTO).isEqualTo(expectedCreditCardResponseDTO);
    }

    @Test
    void findAll() {
        //Arrange
        CreditCard cardSaved1 = CreditCard.builder()
                .id(1L)
                .number("1234")
                .typeCard(TypeCard.VISA)
                .build();
        CreditCard cardSaved2 = CreditCard.builder()
                .id(2L)
                .number("5678")
                .typeCard(TypeCard.MASTER_CARD)
                .build();
        CreditCardResponseDTO responseDTO1 = new CreditCardResponseDTO(1L,"1234","VISA");
        CreditCardResponseDTO responseDTO2 = new CreditCardResponseDTO(2L,"5678","MASTER_CARD");
        List<CreditCard> cards = Arrays.asList(cardSaved1,cardSaved2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<CreditCard> page = new PageImpl<>(cards, pageable, cards.size());
        when(creditCardRepository.findAll(pageable)).thenReturn(page);
        when(creditCardDTOMapper.apply(any(CreditCard.class)))
                .thenReturn(responseDTO1)
                .thenReturn(responseDTO2);
        //Act
        List<CreditCardResponseDTO> responseDTOS = creditCardServiceImp.findAll(0,10);
        //Assert
        assertNotNull(responseDTOS);
        assertEquals(2,responseDTOS.size());
        assertEquals(responseDTO1,responseDTOS.get(0));
        assertEquals(responseDTO2,responseDTOS.get(1));
        verify(creditCardRepository,times(1)).findAll(pageable);
        verify(creditCardDTOMapper,times(2)).apply(any(CreditCard.class));
    }
    @Test
    void findAllEmptyList(){
        //Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<CreditCard> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(creditCardRepository.findAll(pageable)).thenReturn(emptyPage);
        //Act
        CreditCardException exception =  assertThrows(CreditCardException.class, ()->creditCardServiceImp.findAll(0,10));
        //Assertion
        assertEquals("CREDIT_CARD_EXCEPTION: The list of card is null",exception.getMessage());
    }

    @Test
    void findCardsByType() {
        //Arrange
        CreditCard cardSaved1 = CreditCard.builder()
                .id(1L)
                .number("1234")
                .typeCard(TypeCard.VISA)
                .build();
        CreditCard cardSaved2 = CreditCard.builder()
                .id(2L)
                .number("5678")
                .typeCard(TypeCard.VISA)
                .build();
        CreditCardResponseDTO responseDTO1 = new CreditCardResponseDTO(1L,"1234","VISA");
        CreditCardResponseDTO responseDTO2 = new CreditCardResponseDTO(2L,"5678","VISA");
        List<CreditCard> cards = Arrays.asList(cardSaved1,cardSaved2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<CreditCard> page = new PageImpl<>(cards, pageable, cards.size());
        when(creditCardRepository.findCreditCardsByTypeCard(pageable,TypeCard.VISA)).thenReturn(page);
        when(creditCardDTOMapper.apply(any(CreditCard.class)))
                .thenReturn(responseDTO1)
                .thenReturn(responseDTO2);
        //Act
        List<CreditCardResponseDTO> responseDTOS = creditCardServiceImp.findCardsByType(0,10,"VISA");
        //Assertions
        assertNotNull(responseDTOS);
        verify(creditCardRepository,times(1)).findCreditCardsByTypeCard(PageRequest.of(0,10),TypeCard.VISA);
        verify(creditCardDTOMapper,times(2)).apply(any(CreditCard.class));

    }

    @Test
    void removeExistingCard() {
        //Arrange
        Long id = 1L;
        when(creditCardRepository.existsById(id)).thenReturn(true);
        //Act
        boolean result = creditCardServiceImp.remove(id);
        //Assertion
        Assertions.assertThat(result).isTrue();
        verify(creditCardRepository,times(1)).deleteById(id);
    }
    @Test
    void removeNotExistingCard() {
        //Arrange
        Long id = 1L;
        when(creditCardRepository.existsById(id)).thenReturn(false);
        //Act
        CreditCardException exception = assertThrows(CreditCardException.class, ()->creditCardServiceImp.remove(id));
        //Assertion
        assertEquals("CREDIT_CARD_EXCEPTION: The card fetched to delete doesn't exist",exception.getMessage());
        verify(creditCardRepository,never()).deleteById(id);
    }
}