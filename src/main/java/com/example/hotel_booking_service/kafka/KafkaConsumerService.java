package com.example.hotel_booking_service.kafka;

import com.example.hotel_booking_service.kafka.dto.KafkaBookingEvent;
import com.example.hotel_booking_service.kafka.dto.KafkaUserRegistrationEvent;
import com.example.hotel_booking_service.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final StatisticsService statisticsService;

    @KafkaListener(topics = "user_registration", groupId = "hotel_service", containerFactory = "kafkaListenerContainerFactory")
    public void consumeUserRegistration(KafkaUserRegistrationEvent event) {
        statisticsService.saveUserRegistration(event);
    }

    @KafkaListener(topics = "room_booking", groupId = "hotel_service", containerFactory = "kafkaListenerContainerFactory")
    public void consumeRoomBooking(KafkaBookingEvent event) {
        statisticsService.saveBooking(event);
    }
}
