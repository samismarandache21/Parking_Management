package com.example.restanta.model;

public enum Status {
    PENDING, BOOKED, EXITED;

    public static Status fromString(String value) {
        for (Status status : values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}
