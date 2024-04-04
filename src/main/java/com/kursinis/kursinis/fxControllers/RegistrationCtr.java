package com.kursinis.kursinis.fxControllers;
import com.kursinis.kursinis.StartGui;
import com.kursinis.kursinis.hibernateControllers.GenericHib;
import com.kursinis.kursinis.hibernateControllers.UserHib;
import com.kursinis.kursinis.model.User;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/*import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;*/

public class RegistrationCtr {

    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField repeatPasswordField;
    @FXML
    public TextField nameField;
    @FXML
    public TextField surnameField;
    @FXML
    public TextField homeAddressField;
    @FXML
    public TextField cardNoField;
    @FXML
    public DatePicker birthDateField;
    public Label usernameExistsLabel;
    public Label passwordNoMatch;
    public Label enterNameLabel;
    public Label enterSurnameLabel;
    public Label enterAddressLabel;
    public Label enterCardLabel;
    public Label enterDateLabel;
    public Label enterPasswordLabel;
    public TextField addressField;
    private EntityManagerFactory entityManagerFactory;
    private UserHib userHib;
    private GenericHib genericHib;
    public void setData(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        userHib = new UserHib(entityManagerFactory);
    }

    public void createUser() {
        if (loginField.getText().isEmpty() || passwordField.getText().isEmpty() || birthDateField.getValue() == null || nameField.getText().isEmpty() || surnameField.getText().isEmpty() || addressField.getText().isEmpty() || cardNoField.getText().isEmpty()) {
            JavaAlerts.generateAlert(Alert.AlertType.WARNING, "Registration Error", "Please fill in all the fields!", "All fields are mandatory.");
        } else if (userHib.checkForRepeatingNames(loginField.getText()) != null) {
            JavaAlerts.generateAlert(Alert.AlertType.WARNING, "Registration Error", "Username already exists!", "Please choose another login");
            return;
        } else {
            userHib.createUser(new User(loginField.getText(), Hashing.hashing(passwordField.getText()), birthDateField.getValue(), nameField.getText(), surnameField.getText(), "Customer", addressField.getText(), cardNoField.getText(), null));
            JavaAlerts.generateAlert(Alert.AlertType.INFORMATION, "Account Creation", "Your account has been created successfully!", "You can now login.");
            try {
                returnToLogin();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void returnToLogin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("login.fxml"));
        Parent parent = fxmlLoader.load();
        LoginCtr loginController = fxmlLoader.getController();
        loginController.setData(entityManagerFactory);
        Scene scene = new Scene(parent);
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setTitle("loginas");
        stage.setScene(scene);
        stage.show();
    }
}





