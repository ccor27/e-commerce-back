package com.ccor.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
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
    @Lob
    private String picturePath;
    @Min(0)
    private int amount;
    @Min(0)
    private int pricePerUnit;
    @Column(unique = true)
    @Size(min = 10,max = 20)
    private String barCode;
    private boolean enableProduct;
    @Version
    private Long version;
}
