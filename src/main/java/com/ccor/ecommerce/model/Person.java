package com.ccor.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
public abstract class Person{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min=10,max=50)
    private String name;
    @Size(min = 10,max = 50)
    private String lastName;
    @Size(min=9,max=15)
    private String cellphone;
    @Column(unique = true)
    private String email;
}
