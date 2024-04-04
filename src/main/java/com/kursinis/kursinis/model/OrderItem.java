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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @LazyCollection(LazyCollectionOption.FALSE)
    private Cart cart;
    private LocalDate dateCreated;;
    public String orderStatus;
    @ManyToOne
    private User user;
    @OneToMany (mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;
    private double rating;

    public OrderItem(Cart cart, LocalDate dateCreated, String orderStatus) {
        this.cart = cart;
        this.dateCreated = dateCreated;
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "Order id: " + id + " Date: " + dateCreated + " status: " + orderStatus + " User ID: " + user.getName() + " Rating: " + rating;
    }
}
