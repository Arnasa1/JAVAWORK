package com.kursinis.kursinis.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String commentTitle;
    private String commentBody;
    @ManyToOne
    private User user;
    @ManyToOne
    private OrderItem order;
    @OneToMany(mappedBy = "parentReview", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Review> reviewReply;
    @ManyToOne
    private Review parentReview;

    public Review(String commentTitle, String commentBody, User user, OrderItem order, Review parentReview) {
        this.commentTitle = commentTitle;
        this.commentBody = commentBody;
        this.user = user;
        this.order = order;
        this.parentReview = parentReview;
    }

    @Override
    public String toString() {
        return "By: " + user.getName() + " Title: " + commentTitle + " Comment: " + commentBody;
    }
}