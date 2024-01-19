package com.ccor.ecommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
public class CanceledSale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long canceledSaleId;
    private Long saleId;
    private Long paymentId;
    private Date createAt;
}
