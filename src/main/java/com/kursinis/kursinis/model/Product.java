package com.kursinis.kursinis.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Product implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
  @Column(unique = true)
String title;
String description;
String manufacturer;
int price;
String type;
@ManyToOne
Cart cart;
@ManyToOne
Warehouse warehouse;
@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
@LazyCollection(LazyCollectionOption.FALSE)
List<Comment> comments;




  public Product(String title, String description, String manufacturer, String type, int price, Warehouse warehouse) {
    this.title = title;
    this.description = description;
    this.manufacturer = manufacturer;
    this.type = type;
    this.price = price;
    this.warehouse = warehouse;
  }
  public String toString() {
    return "Title:" + title + " Description: " + description + " Manufacturer: " + manufacturer + " Type: " + type + " Price: " + price;
  }



}
