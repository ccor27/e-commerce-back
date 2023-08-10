package com.ccor.ecommerce.service.mapper;

import com.ccor.ecommerce.model.Payment;
import com.ccor.ecommerce.model.ProductSold;
import com.ccor.ecommerce.model.Sale;
import com.ccor.ecommerce.model.dto.ProductSoldResponseDTO;
import com.ccor.ecommerce.model.dto.SaleResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SaleDTOMapper implements Function<Sale, SaleResponseDTO> {
    @Autowired
    private ProductSoldDTOMapper productSoldDTOMapper;
    @Override
    public SaleResponseDTO apply(Sale sale) {
        return new SaleResponseDTO(
                sale.getId(),
                sale.getConcept(),
                productSoldResponseDTOS(sale.getProductsSold()),
                sale.getCreateAt(),
                existPayment(sale.getPayment())
        );
    }
    private Long existPayment(Payment payment){
        return payment!=null ? payment.getId() : null;
    }
    private List<ProductSoldResponseDTO> productSoldResponseDTOS(List<ProductSold> list) {
        if (list != null || list.size() > 0) {
            return list.stream().map(productSold -> {
                return productSoldDTOMapper.apply(productSold);
            }).collect(Collectors.toList());
        } else {
            return null;
        }
    }
}
