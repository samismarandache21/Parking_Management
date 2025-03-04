package com.example.restanta.controller;

import com.example.restanta.repository.BookingDBRepository;
import com.example.restanta.service.BookingService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class SignUpController {
    @FXML
    private Button newClientButton;

    @FXML
    public void initialize() {
        newClientButton.setOnAction(event -> openMainWindow());
    }

    private void openMainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/restanta/main-view.fxml"));

            // Creăm un service pentru fiecare fereastră deschisă
            BookingService bookingService = new BookingService(new BookingDBRepository(
                    "jdbc:postgresql://localhost:5432/restanta", "postgres", "postgres"));

            // Setăm Factory pentru a folosi constructorul cu BookingService
            loader.setControllerFactory(param -> new MainController(bookingService));

            Scene scene = new Scene(loader.load(), 400, 300);

            Stage stage = new Stage();
            stage.setTitle("Parking Session");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
