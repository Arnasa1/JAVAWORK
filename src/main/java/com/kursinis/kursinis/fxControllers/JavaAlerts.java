package com.kursinis.kursinis.fxControllers;

import javafx.scene.control.Alert;

public class JavaAlerts {
    public static void generateAlert(Alert.AlertType alertType, String title, String header, String content){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}