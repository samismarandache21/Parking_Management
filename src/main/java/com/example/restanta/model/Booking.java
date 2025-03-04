package com.example.restanta.model;

import java.time.LocalDateTime;

public class Booking extends Entity<Integer> {
    private String licensePlateNumber;
    private int bookingTimeMinutes;
    private Status status;
    private LocalDateTime creationDate;
    private LocalDateTime bookingStartDate;
    private LocalDateTime bookingEndDate;
    private int parkingSlot;

    public Booking(Integer id, String licensePlateNumber, int bookingTimeMinutes, Status status,
                   LocalDateTime creationDate, LocalDateTime bookingStartDate,
                   LocalDateTime bookingEndDate, int parkingSlot) {
        super(id);
        this.licensePlateNumber = licensePlateNumber;
        this.bookingTimeMinutes = bookingTimeMinutes;
        this.status = status;
        this.creationDate = creationDate;
        this.bookingStartDate = bookingStartDate;
        this.bookingEndDate = bookingEndDate;
        this.parkingSlot = parkingSlot;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public int getBookingTimeMinutes() {
        return bookingTimeMinutes;
    }

    public void setBookingTimeMinutes(int bookingTimeMinutes) {
        this.bookingTimeMinutes = bookingTimeMinutes;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getBookingStartDate() {
        return bookingStartDate;
    }

    public void setBookingStartDate(LocalDateTime bookingStartDate) {
        this.bookingStartDate = bookingStartDate;
    }

    public LocalDateTime getBookingEndDate() {
        return bookingEndDate;
    }

    public void setBookingEndDate(LocalDateTime bookingEndDate) {
        this.bookingEndDate = bookingEndDate;
    }

    public int getParkingSlot() {
        return parkingSlot;
    }

    public void setParkingSlot(int parkingSlot) {
        this.parkingSlot = parkingSlot;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", licensePlateNumber='" + licensePlateNumber + '\'' +
                ", bookingTimeMinutes=" + bookingTimeMinutes +
                ", status=" + status +
                ", creationDate=" + creationDate +
                ", bookingStartDate=" + bookingStartDate +
                ", bookingEndDate=" + bookingEndDate +
                ", parkingSlot=" + parkingSlot +
                '}';
    }
}
