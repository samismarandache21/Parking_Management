package com.example.restanta.controller;

import com.example.restanta.model.Booking;
import com.example.restanta.model.Status;
import com.example.restanta.service.BookingService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class MainController {
    @FXML
    private TableView<Booking> bookingTableView;
    @FXML
    private TableColumn<Booking, Integer> idColumn;
    @FXML
    private TableColumn<Booking, String> licenseColumn;
    @FXML
    private TableColumn<Booking, Integer> timeColumn;
    @FXML
    private TableColumn<Booking, Status> statusColumn;
    @FXML
    private TableColumn<Booking, LocalDateTime> creationColumn;
    @FXML
    private TableColumn<Booking, LocalDateTime> startDateColumn;
    @FXML
    private TableColumn<Booking, LocalDateTime> endDateColumn;
    @FXML
    private TableColumn<Booking, Integer> parkingSlotColumn;


    @FXML
    private TextField licensePlateField;
    @FXML
    private TextField timeField;
    @FXML
    private Button sendRequestButton;

    private final BookingService bookingService;
    private final ObservableList<Booking> sessionBookings;

    public MainController(BookingService bookingService) {
        this.bookingService = bookingService;
        this.sessionBookings = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        System.out.println("MainController initialized.");

        sendRequestButton.setOnAction(event -> handleSendRequest());

        // Configurăm corect coloanele din tabel
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        licenseColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLicensePlateNumber()));
        timeColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBookingTimeMinutes()).asObject());
        statusColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStatus()));
        creationColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCreationDate()));
        startDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getBookingStartDate()));
        endDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getBookingEndDate()));
        parkingSlotColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getParkingSlot()).asObject());


        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> bookingService.processParking(2), 0, 5, TimeUnit.SECONDS);
        // Setăm lista în tabel
        bookingTableView.setItems(sessionBookings);
    }

    private void handleSendRequest() {
        String licensePlate = licensePlateField.getText();
        String timeText = timeField.getText();

        if (licensePlate.isEmpty() || timeText.isEmpty()) {
            System.out.println("Fields cannot be empty.");
            return;
        }

        try {
            int bookingTime = Integer.parseInt(timeText);
            Booking newBooking = new Booking(
                    null, // ID-ul este setat automat de DB
                    licensePlate,
                    bookingTime,
                    Status.PENDING,
                    LocalDateTime.now(),
                    null,
                    null,
                    0
            );

            bookingService.createBooking(licensePlate, bookingTime).ifPresent(booking -> {
                sessionBookings.add(booking);
                updateListView();
            });

            licensePlateField.clear();
            timeField.clear();
        } catch (NumberFormatException e) {
            System.err.println("Invalid input for time.");
        }
    }

    private void updateListView() {
        bookingTableView.refresh();
    }
}
