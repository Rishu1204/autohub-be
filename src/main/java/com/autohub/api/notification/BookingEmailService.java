
package com.autohub.api.notification;

import com.autohub.api.booking.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class BookingEmailService {

    private static final Logger log =
            LoggerFactory.getLogger(BookingEmailService.class);

    private final RestClient restClient;
    private final String apiKey;
    private final String fromEmail;
    private final String toEmail;

    public BookingEmailService(
            @Value("${resend.api-key:}") String apiKey,
            @Value("${app.mail.from:onboarding@resend.dev}") String fromEmail,
            @Value("${app.booking.to-email:}") String toEmail) {

        this.apiKey = apiKey;
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
        this.restClient = RestClient.create();
    }

    public void sendBookingNotification(Booking booking) {
        if (apiKey.isBlank() || toEmail.isBlank()
                || fromEmail.isBlank()) {
            log.warn(
                    "Resend email is not configured; booking id={} was saved but no email was sent.",
                    booking.getId());
            return;
        }

        String message = """
                A new booking request was submitted.

                Booking ID: %s
                Name: %s
                Email: %s
                Phone: %s
                Service: %s
                Preferred date: %s

                Message:
                %s
                """.formatted(
                booking.getId(),
                booking.getName(),
                booking.getEmail(),
                booking.getPhone(),
                booking.getService(),
                booking.getPreferredDate() == null
                        ? "Not specified"
                        : booking.getPreferredDate(),
                booking.getMessage() == null
                        || booking.getMessage().isBlank()
                        ? "Not provided"
                        : booking.getMessage()
        );

        Map<String, Object> request = Map.of(
                "from", "AutoHub <" + fromEmail + ">",
                "to", List.of(toEmail),
                "reply_to", booking.getEmail(),
                "subject", "AutoHub booking request #"
                        + booking.getId() + " - "
                        + booking.getService(),
                "text", message
        );

        try {
            restClient.post()
                    .uri("https://api.resend.com/emails")
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();

            log.info(
                    "Booking notification accepted by Resend for booking id={}",
                    booking.getId());

        } catch (Exception ex) {
            log.error(
                    "Booking id={} was saved, but Resend email delivery request failed.",
                    booking.getId(), ex);
        }
    }
}
