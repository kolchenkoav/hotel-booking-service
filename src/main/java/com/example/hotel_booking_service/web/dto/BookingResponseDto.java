package com.example.hotel_booking_service.web.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingResponseDto {
    private Long bookingId;
    private Long userId;
    private Long roomId;
    private LocalDate checkIn;
    private LocalDate checkOut;
}
