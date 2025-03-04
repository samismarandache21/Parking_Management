package com.example.restanta.repository;

import com.example.restanta.model.Booking;
import com.example.restanta.model.Status;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingDBRepository extends DBRepository<Integer, Booking> {

    public BookingDBRepository(String connectionString, String user, String password) {
        super(connectionString, user, password);
    }

    @Override
    protected void read() {
        entities.clear();
        String query = "SELECT * FROM booking";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Booking booking = extractBooking(resultSet);
                entities.put(booking.getId(), booking);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading bookings from database", e);
        }
        refreshLastId();
    }

    @Override
    protected void refreshLastId() {
        String query = "SELECT MAX(id) FROM booking";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                lastId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error refreshing last ID", e);
        }
    }

    @Override
    public Optional<Booking> save(Booking booking) {
        String query = "INSERT INTO booking (license_plate_number, booking_time_minutes, status, creation_date, booking_start_date, booking_end_date, parking_slot) " +
                "VALUES (?, ?, ?::status, ?, ?, ?, ?) RETURNING id";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, booking.getLicensePlateNumber());
            statement.setInt(2, booking.getBookingTimeMinutes());
            statement.setString(3, booking.getStatus().name());
            statement.setTimestamp(4, Timestamp.valueOf(booking.getCreationDate()));
            statement.setTimestamp(5, booking.getBookingStartDate() != null ? Timestamp.valueOf(booking.getBookingStartDate()) : null);
            statement.setTimestamp(6, booking.getBookingEndDate() != null ? Timestamp.valueOf(booking.getBookingEndDate()) : null);
            statement.setObject(7, booking.getParkingSlot() != 0 ? booking.getParkingSlot() : null);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                booking.setId(resultSet.getInt(1));
                entities.put(booking.getId(), booking);
                refreshLastId();
                return Optional.of(booking);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving booking", e);
        }
        return Optional.empty();
    }

    private Booking extractBooking(ResultSet resultSet) throws SQLException {
        return new Booking(
                resultSet.getInt("id"),
                resultSet.getString("license_plate_number"),
                resultSet.getInt("booking_time_minutes"),
                Status.fromString(resultSet.getString("status")),
                resultSet.getTimestamp("creation_date").toLocalDateTime(),
                resultSet.getTimestamp("booking_start_date") != null ? resultSet.getTimestamp("booking_start_date").toLocalDateTime() : null,
                resultSet.getTimestamp("booking_end_date") != null ? resultSet.getTimestamp("booking_end_date").toLocalDateTime() : null,
                resultSet.getObject("parking_slot") != null ? resultSet.getInt("parking_slot") : 0
        );
    }

    public void allocateNextBooking(int totalParkingSlots) {
        String findFreeSlotQuery = """
        SELECT gs.slot 
        FROM generate_series(1, ?) AS gs(slot) 
        WHERE NOT EXISTS (
            SELECT 1 FROM booking 
            WHERE booking.parking_slot = gs.slot 
            AND booking.status = 'BOOKED'
        )
        LIMIT 1
    """;

        String updateBookingQuery = """
        UPDATE booking
        SET status = 'BOOKED', parking_slot = ?, booking_start_date = NOW()
        WHERE id = (
            SELECT id FROM booking WHERE status = 'PENDING' 
            ORDER BY creation_date ASC LIMIT 1
        )
    """;

        try (Connection connection = getConnection();
             PreparedStatement findSlotStmt = connection.prepareStatement(findFreeSlotQuery);
             PreparedStatement updateBookingStmt = connection.prepareStatement(updateBookingQuery)) {

            findSlotStmt.setInt(1, totalParkingSlots);
            ResultSet resultSet = findSlotStmt.executeQuery();

            if (resultSet.next()) {
                int freeSlot = resultSet.getInt("slot");
                updateBookingStmt.setInt(1, freeSlot);
                updateBookingStmt.executeUpdate();
                //System.out.println("Allocated slot " + freeSlot + " to next pending booking.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error allocating parking slot", e);
        }
    }

    public void releaseExpiredBookings() {
        String query = """
        UPDATE booking 
        SET status = 'EXITED', booking_end_date = NOW()
        WHERE status = 'BOOKED' 
        AND NOW() >= (booking_start_date + INTERVAL '1 minute' * booking_time_minutes)
    """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            int updatedRows = statement.executeUpdate();
            if (updatedRows > 0) {
                //System.out.println(updatedRows + " bookings have been marked as EXITED.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error releasing expired bookings", e);
        }
    }



}
