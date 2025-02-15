package com.example.hotel_booking_service.kafka;

import com.example.hotel_booking_service.kafka.dto.KafkaBookingEvent;
import com.example.hotel_booking_service.kafka.dto.KafkaUserRegistrationEvent;
import com.example.hotel_booking_service.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Сервис для потребления событий из Kafka.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final StatisticsService statisticsService;

    /**
     * Метод для потребления событий регистрации пользователя.
     *
     * @param event событие регистрации пользователя
     */
    @KafkaListener(topics = "user_registration", groupId = "hotel_service", containerFactory = "kafkaListenerContainerFactory")
    public void consumeUserRegistration(KafkaUserRegistrationEvent event) {
        log.info("KafkaConsumerService -> consumeUserRegistration(KafkaUserRegistrationEvent event) -> event.getUserId(): {}", event.getUserId());
        statisticsService.saveUserRegistration(event);
    }

    /**
     * Метод для потребления событий бронирования комнаты.
     *
     * @param event событие бронирования комнаты
     */
    @KafkaListener(topics = "room_booking", groupId = "hotel_service", containerFactory = "kafkaListenerContainerFactory")
    public void consumeRoomBooking(KafkaBookingEvent event) {
        log.info("Получено событие бронирования: {}", event);
        statisticsService.saveBooking(event);
    }
}
