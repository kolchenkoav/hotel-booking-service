package com.example.hotel_booking_service.web.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequestDto {
    @NotNull
    private Long userId;

    @NotNull
    private Long roomId;

    @NotNull
    @Future
    private LocalDate checkIn;

    @NotNull
    @Future
    private LocalDate checkOut;
}
