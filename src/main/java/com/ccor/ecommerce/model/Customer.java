package com.ccor.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import javax.management.ConstructorParameters;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
public class Customer extends Person{
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "history_id")
    private History history;
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id")
    private List<Address> address;
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "card_id")
    private List<CreditCard> cards;
    @OneToMany(mappedBy = "customer",cascade = CascadeType.PERSIST)
    private List<Token> tokens;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.PERSIST)
    private List<ConfirmationToken> confirmationTokens;
    private boolean enableUser;
    /**
     * @ElementCollection to indicate that 'roles'
     * is a collection of elements, the 'targetClass' attribute specifies the type
     * of elements in the collections(Role enum)
     * @CollectionTable annotation defies the mapping for the join table that stores
     * the roles for each customer
     * @Colum annotation specifies the name of the column that stores the role values
     * @Enumerated(EnumType.String) annotation indicates that the enum values should be stored
     * as string in the database
     */
    @ElementCollection(targetClass = Role.class)
    @CollectionTable(
            name = "customer_roles",
            joinColumns = @JoinColumn(name = "customer_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private List<Role> roles;
}
