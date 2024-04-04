package com.kursinis.kursinis.fxControllers;

import com.kursinis.kursinis.StartGui;
import com.kursinis.kursinis.hibernateControllers.UserHib;
import com.kursinis.kursinis.model.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginCtr implements Initializable {
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    public int userId;
    private EntityManagerFactory entityManagerFactory;
    private UserHib userHib;

    public void registerNewUser() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("registration.fxml"));
        Parent parent = fxmlLoader.load();
        RegistrationCtr registrationCtr = fxmlLoader.getController();
        registrationCtr.setData(entityManagerFactory);
        Scene scene = new Scene(parent);
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setTitle("Registration");
        stage.setScene(scene);
        stage.show();
    }

    public void validateAndConnect() throws IOException {
        userHib = new UserHib(entityManagerFactory);
        User user = userHib.getUserByCredentials(loginField.getText(), passwordField.getText());
        if (user != null && user.getType().equals("Admin")) {
            FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("main-shop.fxml"));
            Parent parent = fxmlLoader.load();
            MainShop mainShopController = fxmlLoader.getController();
            mainShopController.setData(entityManagerFactory, user);
            Scene scene = new Scene(parent);
            Stage stage = (Stage) loginField.getScene().getWindow();
            stage.setTitle("Admin EShop");
            stage.setScene(scene);
            stage.show();
        }
        else if(user!= null && user.getType().equals("Customer"))
        {
            FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("Customer.fxml"));
            Parent parent = fxmlLoader.load();
            CustomerCtr customerCtr = fxmlLoader.getController();
            customerCtr.setData(entityManagerFactory, user);
            Scene scene = new Scene(parent);
            Stage stage = (Stage) loginField.getScene().getWindow();
            stage.setTitle("Customer EShop");
            stage.setScene(scene);
            stage.show();
        }
        else if(user!=null && user.getType().equals("Employee")){
            FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("employee.fxml"));
            Parent parent = fxmlLoader.load();
            EmployeeCtr employeeCtr = fxmlLoader.getController();
            employeeCtr.setData(entityManagerFactory, user);
            Scene scene = new Scene(parent);
            Stage stage = (Stage) loginField.getScene().getWindow();
            stage.setTitle("Employee EShop");
            stage.setScene(scene);
            stage.show();
        }
        else {
            JavaAlerts.generateAlert(Alert.AlertType.INFORMATION, "Login Error", "Wrong login or password", "Please check credentials, no such user");
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        entityManagerFactory = Persistence.createEntityManagerFactory("kursinioDB");
    }

    public void setData(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;

    }
}
