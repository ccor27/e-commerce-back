package com.ccor.ecommerce.service.mapper;

import com.ccor.ecommerce.model.CanceledSale;
import com.ccor.ecommerce.model.dto.CanceledSaleResponseDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class CanceledSaleDTOMapper implements Function<CanceledSale, CanceledSaleResponseDTO> {
    @Override
    public CanceledSaleResponseDTO apply(CanceledSale canceledSale) {
        return new CanceledSaleResponseDTO(
                canceledSale.getCanceledSaleId(),
                canceledSale.getSaleId(),
                canceledSale.getPaymentId(),
                canceledSale.getCreateAt()
        );
    }
}
