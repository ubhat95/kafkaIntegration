module com.uttam.kafkaintegration {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.uttam.kafkaintegration to javafx.fxml;
    exports com.uttam.kafkaintegration;
}