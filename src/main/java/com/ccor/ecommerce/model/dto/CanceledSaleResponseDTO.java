package com.ccor.ecommerce.model.dto;

import java.util.Date;

public record CanceledSaleResponseDTO(
         Long canceledSaleId,
         Long saleId,
         Long paymentId,
         Date createAt
)
{}
