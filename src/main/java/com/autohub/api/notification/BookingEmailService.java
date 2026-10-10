package com.autohub.api.notification;

import com.autohub.api.booking.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class BookingEmailService {

    private static final Logger log = LoggerFactory.getLogger(BookingEmailService.class);
    private final JavaMailSender mailSender;
    @Value("${app.booking.to-email:}") private String toEmail;
    @Value("${spring.mail.username:}") private String fromEmail;

    public BookingEmailService(JavaMailSender mailSender) { this.mailSender = mailSender; }

    public void sendBookingNotification(Booking booking) {
        if (toEmail.isBlank() || fromEmail.isBlank()) {
            log.warn("Booking email is not configured; booking id={} was saved but no email was sent.", booking.getId());
            return;
        }
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(fromEmail);
        mail.setTo(toEmail);
        mail.setReplyTo(booking.getEmail());
        mail.setSubject("AutoHub booking request #" + booking.getId() + " — " + booking.getService());
        StringBuilder message = new StringBuilder();

        message.append("A new booking request was submitted.\n\n")
                .append("Booking ID: ").append(booking.getId()).append("\n")
                .append("Name: ").append(booking.getName()).append("\n")
                .append("Email: ").append(booking.getEmail()).append("\n")
                .append("Phone: ").append(booking.getPhone()).append("\n")
                .append("Service: ").append(booking.getService()).append("\n")
                .append("Preferred date: ")
                .append(booking.getPreferredDate() == null
                        ? "Not specified"
                        : booking.getPreferredDate())
                .append("\n\n")
                .append("Message:\n")
                .append(booking.getMessage().isBlank() ? " " : booking.getMessage());

        mail.setText(message.toString());
        try {
            mailSender.send(mail);
        } catch (Exception ex) {
            log.error("Booking id={} saved, but notification email failed.", booking.getId(), ex);
        }
    }
}
