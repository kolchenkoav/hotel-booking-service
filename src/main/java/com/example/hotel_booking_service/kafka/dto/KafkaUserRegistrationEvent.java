package com.example.hotel_booking_service.kafka.dto;

import lombok.Data;

@Data
public class KafkaUserRegistrationEvent {
    private Long userId;
}
