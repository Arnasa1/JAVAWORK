package com.kursinis.kursinis.tableParameters;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class userTableParameters {
    SimpleIntegerProperty id = new SimpleIntegerProperty();

    SimpleStringProperty name = new SimpleStringProperty();
    SimpleStringProperty surname = new SimpleStringProperty();
    SimpleStringProperty login = new SimpleStringProperty();
    SimpleStringProperty password = new SimpleStringProperty();

    SimpleStringProperty cardNo = new SimpleStringProperty();
    SimpleStringProperty type = new SimpleStringProperty();
    SimpleStringProperty isAdmin = new SimpleStringProperty();
    SimpleStringProperty address = new SimpleStringProperty();
    SimpleStringProperty birthDate = new SimpleStringProperty();

    public userTableParameters(SimpleIntegerProperty id, SimpleStringProperty name, SimpleStringProperty surname, SimpleStringProperty login, SimpleStringProperty password, SimpleStringProperty cardNo, SimpleStringProperty type, SimpleStringProperty isAdmin, SimpleStringProperty address, SimpleStringProperty birthDate) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.cardNo = cardNo;
        this.type = type;
        this.isAdmin = isAdmin;
        this.address = address;
        this.birthDate = birthDate;
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

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSurname() {
        return surname.get();
    }

    public SimpleStringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public String getLogin() {
        return login.get();
    }

    public SimpleStringProperty loginProperty() {
        return login;
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getCardNo() {
        return cardNo.get();
    }

    public SimpleStringProperty cardNoProperty() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo.set(cardNo);
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

    public String getIsAdmin() {
        return isAdmin.get();
    }

    public SimpleStringProperty isAdminProperty() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin.set(isAdmin);
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getBirthDate() {
        return birthDate.get();
    }

    public SimpleStringProperty birthDateProperty() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate.set(birthDate);
    }

    public userTableParameters() {
    }

}
