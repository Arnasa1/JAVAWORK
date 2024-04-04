package com.kursinis.kursinis.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate dateCreated;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = false)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Product> itemsInCart;
    @OneToOne
    private User user;
    @OneToOne
    private OrderItem order;




}