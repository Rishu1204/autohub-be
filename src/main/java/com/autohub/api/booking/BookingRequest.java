package com.autohub.api.booking;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookingRequest(
        @NotBlank @Size(min = 2, max = 100) String name,
        @NotBlank @Email @Size(max = 254) String email,
        @NotBlank @Size(min = 7, max = 20) String phone,
        @NotBlank @Size(max = 100) String service,
        @Size(max = 10) String date,
        @Size(max = 1500) String message
) {}
