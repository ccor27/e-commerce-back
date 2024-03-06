package com.ccor.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String idStripePayment;
    @Enumerated(EnumType.STRING)
    private StatusPayment statusPayment;
    private Date createAt;
    private boolean isDeleted;
    @ManyToOne
    @JoinColumn(name = "credit_card_id")
    private CreditCard card;
    @Min(0)
    private int totalPrice;
}
