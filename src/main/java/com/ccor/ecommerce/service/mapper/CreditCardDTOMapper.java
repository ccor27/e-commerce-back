package com.ccor.ecommerce.service.mapper;

import com.ccor.ecommerce.model.CreditCard;
import com.ccor.ecommerce.model.dto.CreditCardResponseDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class CreditCardDTOMapper implements Function<CreditCard, CreditCardResponseDTO> {
    @Override
    public CreditCardResponseDTO apply(CreditCard creditCard) {
        return new CreditCardResponseDTO(
                creditCard.getId(),
                creditCard.getNumber(),
                String.valueOf(creditCard.getTypeCard())
        );
    }
}
