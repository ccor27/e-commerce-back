package com.ccor.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.time.YearMonth;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String holderName;
    @Column(length = 16)
    private String number;
    @Range(min=1,max = 12)
    private int monthExp;
    @Range(min=1000,max = 9999)
    private int yearExp;
    @Column(length = 3)
    private int cvv;
    @Enumerated(EnumType.STRING)
    private TypeCard typeCard;
    
}
