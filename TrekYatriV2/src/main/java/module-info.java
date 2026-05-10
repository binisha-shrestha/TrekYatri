module com.example.trekyatriv2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.trekyatriv2 to javafx.fxml;
    exports com.example.trekyatriv2;
}