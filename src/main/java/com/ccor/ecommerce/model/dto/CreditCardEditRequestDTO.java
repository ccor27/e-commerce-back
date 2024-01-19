package com.ccor.ecommerce.model.dto;

import java.time.YearMonth;

public record CreditCardEditRequestDTO(
        String number,
        String holderName,
        int cvv,
        String type,
        int monthExp,
        int yearExp

) {
}
