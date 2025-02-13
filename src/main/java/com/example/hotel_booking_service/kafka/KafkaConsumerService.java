package com.example.hotel_booking_service.kafka;

import com.example.hotel_booking_service.kafka.dto.KafkaBookingEvent;
import com.example.hotel_booking_service.kafka.dto.KafkaUserRegistrationEvent;
import com.example.hotel_booking_service.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final StatisticsService statisticsService;

    @KafkaListener(topics = "user_registration", groupId = "hotel_service", containerFactory = "kafkaListenerContainerFactory")
    public void consumeUserRegistration(KafkaUserRegistrationEvent event) {
        log.info("Получено событие регистрации: {}", event);
        statisticsService.saveUserRegistration(event);
    }

    @KafkaListener(topics = "room_booking", groupId = "hotel_service", containerFactory = "kafkaListenerContainerFactory")
    public void consumeRoomBooking(KafkaBookingEvent event) {
        log.info("Получено событие бронирования: {}", event);
        statisticsService.saveBooking(event);
    }
}
