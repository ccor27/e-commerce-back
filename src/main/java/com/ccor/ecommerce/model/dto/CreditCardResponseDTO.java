package com.ccor.ecommerce.model.dto;

import java.time.YearMonth;
import java.util.Date;

public record CreditCardResponseDTO(
        Long id,
        String number,
        String type,
        int monthExp,
        int yearExp,
        int cvv
) {
}
