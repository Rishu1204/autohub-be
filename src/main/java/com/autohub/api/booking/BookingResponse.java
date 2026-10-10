package com.autohub.api.booking;

import java.time.Instant;

public record BookingResponse(Long id, String status, String message, Instant createdAt) {
    public static BookingResponse accepted(Booking booking) {
        return new BookingResponse(booking.getId(), "RECEIVED", "Your booking request has been received.", booking.getCreatedAt());
    }
}
