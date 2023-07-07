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
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String concept;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<ProductSold> productsSold;
    @Temporal(TemporalType.DATE)
    private Date createAt;


}
