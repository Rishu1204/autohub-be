package com.autohub.api.booking;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100) private String name;
    @Column(nullable = false, length = 254) private String email;
    @Column(nullable = false, length = 20) private String phone;
    @Column(nullable = false, length = 100) private String service;
    @Column(length = 10) private String preferredDate;
    @Column(length = 1500) private String message;
    @Column(nullable = false) private Instant createdAt;

    protected Booking() {}
    public Booking(BookingRequest request) {
        this.name = request.name().trim(); this.email = request.email().trim();
        this.phone = request.phone().trim(); this.service = request.service().trim();
        this.preferredDate = request.date() == null || request.date().isBlank() ? null : request.date().trim();
        this.message = request.message() == null ? "" : request.message().trim();
        this.createdAt = Instant.now();
    }
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getService() { return service; }
    public String getPreferredDate() { return preferredDate; }
    public String getMessage() { return message; }
    public Instant getCreatedAt() { return createdAt; }
}
