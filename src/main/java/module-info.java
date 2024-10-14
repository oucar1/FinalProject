module org.example.finalproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens org.example.finalproject to javafx.fxml;
    exports org.example.finalproject;
}