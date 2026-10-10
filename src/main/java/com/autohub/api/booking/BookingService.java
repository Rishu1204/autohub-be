package com.autohub.api.booking;

import com.autohub.api.notification.BookingEmailService;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final BookingEmailService emailService;

    public BookingService(BookingEmailService emailService) {
        this.emailService = emailService;
    }

    public BookingResponse create(BookingRequest request) {
        Booking booking = new Booking(request);
        emailService.sendBookingNotification(booking);
        return BookingResponse.accepted(booking);
    }
}
