module org.example.bazedate {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.kordamp.bootstrapfx.core;

    opens org.example.bazedate to javafx.fxml;
    exports org.example.bazedate;
}