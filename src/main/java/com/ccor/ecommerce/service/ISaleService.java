package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.CanceledSale;
import com.ccor.ecommerce.model.ProductStock;
import com.ccor.ecommerce.model.Sale;
import com.ccor.ecommerce.model.dto.*;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import java.util.Date;
import java.util.List;

public interface ISaleService {
    SaleResponseDTO save(SaleRequestDTO saleRequestDTO,Long customerId);
    SaleResponseDTO findById(Long id);
    List<SaleResponseDTO> findAll(Integer offset,Integer pageSize);
    List<Sale> findAllToExport(Integer offset, Integer pageSize);
    List<Sale> findAllToExport();
    List<SaleResponseDTO> findSalesByDate(Date createAt,Integer offset,Integer pageSize);
    List<SaleResponseDTO> findSalesBetweenDate(Date createAtOne,Date createAtTwo,Integer offset,Integer pageSize);

    List<ProductSoldResponseDTO> findProductsSold(Long id,Integer offset,Integer pageSize);
    PaymentResponseDTO findPayment(Long id);
    void cancelSale(Long paymentId);
}
