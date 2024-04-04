package com.kursinis.kursinis.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements Serializable {
    @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(unique = true)
    String login;
    String password;
    LocalDate birthDate;
    String name;
    String surname;
    private String type;
    private String address;
    private String cardNo;
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @LazyCollection(LazyCollectionOption.FALSE)
    List<Comment> myComments;
    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @LazyCollection(LazyCollectionOption.FALSE)
    private Cart myCart;
    @ManyToOne
    private Warehouse warehouse;
    private boolean isAdmin;
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Review> myReviews;
    public User(String login, String password, LocalDate birthDate, String name, String surname, String type, String address, String cardNo, Warehouse warehouse){
        this.login = login;
        this.password = password;
        this.birthDate = birthDate;
        this.name = name;
        this.surname = surname;
        this.type = type;
        this.address = address;
        this.cardNo = cardNo;
        this.warehouse = warehouse;
    }

    public User(int id, String login, String password, LocalDate birthDate) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return type+ " " + name + " " + surname;
    }

    public LocalDate getBday() {
        return birthDate;
    }

    public int getId() {
        return id;
    }
    public void setBday(LocalDate value) {
        this.birthDate = birthDate;
    }
}
