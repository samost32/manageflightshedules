module com.example.manageflightshedules {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.manageflightshedules to javafx.fxml;
    exports com.example.manageflightshedules;
}