package com.ccor.ecommerce.model;

import jakarta.persistence.*;
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
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerFullName;
    @OneToMany
    @JoinTable(name = "history_sale")
    //@JoinColumn(name = "history_id")
    private List<Sale> sales;
    private Date modificationDate;

}
