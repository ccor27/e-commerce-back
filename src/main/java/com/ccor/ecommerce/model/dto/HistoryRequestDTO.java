package com.ccor.ecommerce.model.dto;

import java.util.Date;

public record HistoryRequestDTO(
        Long id_customer,
        Date dateModification
) {
}
