package com.example.restanta;

import com.example.restanta.controller.SignUpController;
import com.example.restanta.repository.BookingDBRepository;
import com.example.restanta.service.BookingService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Conectare la baza de date
        String url = "jdbc:postgresql://localhost:5432/restanta";
        String user = "postgres";
        String password = "postgres";

        // Inițializare repository și service
        BookingDBRepository bookingDBRepository = new BookingDBRepository(url, user, password);
        BookingService bookingService = new BookingService(bookingDBRepository);

        // Inițializare fereastră Sign Up
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/restanta/signup-view.fxml"));
        fxmlLoader.setControllerFactory(param -> new SignUpController());
        Scene scene = new Scene(fxmlLoader.load(), 300, 200);

        stage.setTitle("Sign Up");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
