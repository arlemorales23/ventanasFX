module org.arle.proyectofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    // Exports
    exports org.arle.proyectofx to javafx.graphics, javafx.fxml;
    exports org.arle.proyectofx.controller to javafx.fxml;
    exports org.arle.proyectofx.model;

    // Opens for reflection
    opens org.arle.proyectofx to javafx.fxml, javafx.graphics;
    opens org.arle.proyectofx.controller to javafx.fxml;
    opens org.arle.proyectofx.model; // Abierto para todos los módulos para permitir reflexión
}

