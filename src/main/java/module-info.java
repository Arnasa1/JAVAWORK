module com.kursinis.kursinis {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires jfreechart;
    requires java.desktop;
    requires spring.core;
    requires spring.context;
    requires spring.web;
    requires com.google.gson;

    opens com.kursinis.kursinis to javafx.fxml;
    opens com.kursinis.kursinis.fxControllers to javafx.fxml;
    opens com.kursinis.kursinis.tableParameters to javafx.base;
    opens com.kursinis.kursinis.model to org.hibernate.orm.core, javafx.base;

    exports com.kursinis.kursinis;
    exports com.kursinis.kursinis.fxControllers to javafx.fxml;
}