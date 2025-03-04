package com.example.restanta.service;

import com.example.restanta.model.Booking;
import com.example.restanta.model.Status;
import com.example.restanta.repository.BookingDBRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BookingService {
    private final BookingDBRepository bookingRepository;

    public BookingService(BookingDBRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Optional<Booking> createBooking(String licensePlateNumber, int bookingTimeMinutes) {
        Booking newBooking = new Booking(
                null, // ID-ul va fi setat automat de baza de date
                licensePlateNumber,
                bookingTimeMinutes,
                Status.PENDING,
                LocalDateTime.now(), // creationDate este momentul actual
                null, // bookingStartDate se setează când devine BOOKED
                null, // bookingEndDate se setează când devine EXITED
                0 // parkingSlot se alocă ulterior
        );
        return bookingRepository.save(newBooking);
    }

    public void processParking(int totalParkingSlots) {
        bookingRepository.releaseExpiredBookings();  // Eliberează locurile expirate
        bookingRepository.allocateNextBooking(totalParkingSlots); // Alocă noi rezervări dacă există locuri disponibile
    }


}
