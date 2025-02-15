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
        // Убираем ручное добавление заголовков
        log.info("Sending user registration event: {}", event.getUserId());
        kafkaTemplate.send("user_registration", event);
    }

    public void sendRoomBooking(KafkaBookingEvent event) {
        Message<KafkaBookingEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader("__TypeId__", KafkaBookingEvent.class.getName()) // Указываем заголовок с типом
                .build();
        kafkaTemplate.send("room_booking", message);
    }
}
