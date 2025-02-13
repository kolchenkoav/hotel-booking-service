package com.example.hotel_booking_service.kafka.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class KafkaBookingEvent {
    private Long userId;
    private LocalDate checkIn;
    private LocalDate checkOut;
}
