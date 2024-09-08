module com.example.educationplatform {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.educationplatform to javafx.fxml;
    exports com.example.educationplatform;
}