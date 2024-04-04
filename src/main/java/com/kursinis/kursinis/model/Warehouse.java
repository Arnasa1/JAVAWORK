package com.kursinis.kursinis.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Warehouse implements Serializable {
@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
private String title;
private String address;
@OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
@LazyCollection(LazyCollectionOption.FALSE)
private List<User> managers;
@OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
@LazyCollection(LazyCollectionOption.FALSE)
private List<Product> productsInStock;

    public Warehouse(String title, String address) {
        this.title = title;
        this.address = address;
        this.productsInStock = new ArrayList<>();
        this.managers = new ArrayList<>();
    }
    @Override
    public String toString() {
        return title + "," + address;
    }
}
