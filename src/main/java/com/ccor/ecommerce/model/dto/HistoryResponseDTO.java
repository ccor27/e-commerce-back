package com.ccor.ecommerce.model.dto;

import java.util.Date;
import java.util.List;

public record HistoryResponseDTO(
        Long id,
        Long id_customer,
        List<SaleResponseDTO> sales,
        Date dateModification
) {
}
