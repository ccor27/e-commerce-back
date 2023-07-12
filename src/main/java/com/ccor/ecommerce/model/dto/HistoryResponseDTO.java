package com.ccor.ecommerce.model.dto;

import java.util.Date;
import java.util.List;

public record HistoryResponseDTO(
        Long id,
        List<SaleResponseDTO> sales,
        Date dateModification
) {
}
