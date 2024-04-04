package com.kursinis.kursinis.tableParameters;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class productTableParameters {
    SimpleIntegerProperty id = new SimpleIntegerProperty();
    SimpleStringProperty title = new SimpleStringProperty();
    SimpleStringProperty description = new SimpleStringProperty();
    SimpleStringProperty manufacturer = new SimpleStringProperty();
    SimpleStringProperty type = new SimpleStringProperty();
    SimpleIntegerProperty price = new SimpleIntegerProperty();



    public productTableParameters(SimpleIntegerProperty id, SimpleStringProperty title, SimpleStringProperty description, SimpleStringProperty manufacturer, SimpleStringProperty type, SimpleIntegerProperty price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.manufacturer = manufacturer;
        this.type = type;
        this.price = price;
    }

    public productTableParameters() {
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getManufacturer() {
        return manufacturer.get();
    }

    public SimpleStringProperty manufacturerProperty() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer.set(manufacturer);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public int getPrice() {
        return price.get();
    }

    public SimpleIntegerProperty priceProperty() {
        return price;
    }

    public void setPrice(int price) {
        this.price.set(price);
    }


}
