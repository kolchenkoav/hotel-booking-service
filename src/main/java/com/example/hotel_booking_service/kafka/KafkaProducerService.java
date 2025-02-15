package com.example.hotel_booking_service.kafka;

import com.example.hotel_booking_service.kafka.dto.KafkaBookingEvent;
import com.example.hotel_booking_service.kafka.dto.KafkaUserRegistrationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserRegistration(KafkaUserRegistrationEvent event) {
        Message<KafkaUserRegistrationEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader("__TypeId__", KafkaUserRegistrationEvent.class.getName()) // Указываем заголовок с типом
                .build();
        log.info("KafkaProducerService -> sendUserRegistration(KafkaUserRegistrationEvent event) -> Получено событие регистрации: {}", event);
        log.info("KafkaProducerService -> sendUserRegistration(KafkaUserRegistrationEvent event) -> message.getPayload().getUserId(): {}", message.getPayload().getUserId());
        kafkaTemplate.send("user_registration", message);
    }

    public void sendRoomBooking(KafkaBookingEvent event) {
        Message<KafkaBookingEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader("__TypeId__", KafkaBookingEvent.class.getName()) // Указываем заголовок с типом
                .build();
        kafkaTemplate.send("room_booking", message);
    }
}
