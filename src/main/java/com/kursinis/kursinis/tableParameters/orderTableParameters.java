package com.kursinis.kursinis.tableParameters;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class orderTableParameters {
    SimpleIntegerProperty id = new SimpleIntegerProperty();
    SimpleStringProperty dateCreated = new SimpleStringProperty();
    SimpleStringProperty orderStatus = new SimpleStringProperty();
    SimpleDoubleProperty rating = new SimpleDoubleProperty();


    public void commentTableParameters(SimpleIntegerProperty id, SimpleStringProperty dateCreated, SimpleStringProperty orderStatus, SimpleDoubleProperty rating) {
        this.id = id;
        this.dateCreated = dateCreated;
        this.orderStatus = orderStatus;
        this.rating = rating;

    }
    public int getId() {
        return id.get();
    }

    public SimpleStringProperty dateCreatedProperty() {
        return dateCreated;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getOrderStatus() {
        return orderStatus.get();
    }

    public SimpleStringProperty orderStatusProperty() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus.set(orderStatus);
    }

    public double getRating() {
        return rating.get();
    }

    public SimpleDoubleProperty ratingProperty() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating.set(rating);
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated.set(dateCreated);
    }

    public String getDateCreated() {
        return dateCreated.get();
    }



    public orderTableParameters() {
    }
}
