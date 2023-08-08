package com.ccor.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
public class Customer extends Person implements UserDetails {
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "history_id")
    private History history;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private List<Address> address;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private List<CreditCard> cards;
    @OneToMany(mappedBy = "customer",cascade = CascadeType.PERSIST)
    private List<Token> tokens;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.PERSIST)
    private List<ConfirmationToken> confirmationTokens;
    private boolean enable;
    @Column(unique = true)
    private String username;
    private String pwd;
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
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(
            name = "customer_roles",
            joinColumns = @JoinColumn(name = "customer_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private List<Role> roles;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<? extends GrantedAuthority> collect = roles.stream().map(role -> {
            return new SimpleGrantedAuthority("ROLE_"+role.name());
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public String getPassword() {
        return pwd;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }
}
