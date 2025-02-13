package com.example.hotel_booking_service.kafka;

import com.example.hotel_booking_service.kafka.dto.KafkaBookingEvent;
import com.example.hotel_booking_service.kafka.dto.KafkaUserRegistrationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserRegistration(KafkaUserRegistrationEvent event) {
        Message<KafkaUserRegistrationEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader("__TypeId__", KafkaUserRegistrationEvent.class.getName()) // Указываем заголовок с типом
                .build();
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
