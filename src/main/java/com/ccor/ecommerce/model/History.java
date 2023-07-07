package com.ccor.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long id_customer;
    @OneToMany
    @JoinColumn(name = "sale_id")
    private List<Sale> sales;
    private Date modificationDate;
}
