package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.CreditCard;
import com.ccor.ecommerce.model.TypeCard;
import com.ccor.ecommerce.model.dto.CreditCardRequestDTO;
import com.ccor.ecommerce.model.dto.CreditCardResponseDTO;
import com.ccor.ecommerce.repository.CreditCardRepository;
import com.ccor.ecommerce.service.mapper.CreditCardDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreditCardServiceImp implements ICreditCardService{
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private CreditCardDTOMapper dtoMapper;

    @Override
    public CreditCardResponseDTO save(CreditCardRequestDTO requestDTO) {
        if(requestDTO!=null){
            CreditCard creditCard = CreditCard.builder()
                    .number(requestDTO.number())
                    .typeCard(typeCard(requestDTO.type()))
                    .build();
            return dtoMapper.apply(creditCardRepository.save(creditCard));
        }else{
        return null;
        }
    }
    private TypeCard typeCard(String type){
        return switch (type) {
            case "VISA" -> TypeCard.VISA;
            case "MASTER_CARD" -> TypeCard.MASTER_CARD;
            case "AMERICAN_EXPRESS" -> TypeCard.AMERICAN_EXPRESS;
            default -> null;
        };
    }

    @Override
    public CreditCardResponseDTO edit(CreditCardRequestDTO requestDTO, Long id) {
        CreditCard card = creditCardRepository.findById(id).orElse(null);
        if(card!=null && requestDTO!=null){
            card.setNumber(requestDTO.number());
            card.setTypeCard(typeCard(requestDTO.type()));
            return dtoMapper.apply(creditCardRepository.save(card));
        }else{
            return null;
        }
    }

    @Override
    public CreditCardResponseDTO findById(Long id) {
        CreditCard card = creditCardRepository.findById(id).orElse(null);
        if(card!=null){
            return dtoMapper.apply(card);
        }else{
            return null;
        }
    }

    @Override
    public CreditCardResponseDTO findCardByNumber(String number) {
        CreditCard card = creditCardRepository.findCreditCardByNumber(number).orElse(null);
        return card!=null ? dtoMapper.apply(card) : null;
    }

    @Override
    public List<CreditCardResponseDTO> findAll() {
        List<CreditCard> cards = creditCardRepository.findAll();
        if(cards!=null){
            return cards.stream().map(creditCard -> {
                return dtoMapper.apply(creditCard);
            }).collect(Collectors.toList());
        }else{
            return null;
        }

    }

    @Override
    public List<CreditCardResponseDTO> findCardsByType(String type) {
        List<CreditCard> cards = creditCardRepository.findCreditCardsByTypeCard(typeCard(type));
        if(cards!=null){
            return cards.stream().map(creditCard -> {
                return dtoMapper.apply(creditCard);
            }).collect(Collectors.toList());
        }else{
            return null;
        }
    }

    @Override
    public boolean remove(Long id) {
        if(creditCardRepository.existsById(id)){
            creditCardRepository.deleteById(id);
            return true;
        }else{
            return false;
        }
    }
}
