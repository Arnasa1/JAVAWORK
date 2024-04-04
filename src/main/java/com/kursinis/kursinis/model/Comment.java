package com.kursinis.kursinis.model;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String commentTitle;
    private String commentBody;
    @ManyToOne
    private User user;
    @ManyToOne
    private Product product;
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Comment> replies;
    @ManyToOne
    private Comment parentComment;

    public Comment(String commentTitle, String commentBody, User user, Product product, Comment parentComment) {
        this.commentTitle = commentTitle;
        this.commentBody = commentBody;
        this.user = user;
        this.product = product;
        this.parentComment = parentComment;
    }

    public void setUserId(int userId) {
        this.user.setId(userId);
    }

    @Override
    public String toString() {
        return "By: " + user.getName() + " Title: " + commentTitle + " Comment: " + commentBody;
    }
}