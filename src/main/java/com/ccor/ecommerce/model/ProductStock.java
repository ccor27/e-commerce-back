package com.ccor.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name","barCode"})
})
public class ProductStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private int amount;
    private double pricePerUnit;
    @Column(unique = true)
    private String barCode;
    private boolean enableProduct;
}
