package com.example.hotel_booking_service.kafka;

import com.example.hotel_booking_service.kafka.dto.KafkaUserRegistrationEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    public NewTopic userRegistrationTopic() {
        return new NewTopic("user_registration", 1, (short) 1);
    }

    @Bean
    public NewTopic bookingTopic() {
        return new NewTopic("room_booking", 1, (short) 1);
    }

    // Producer с JSON сериализацией
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, true);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

//    @Bean
//    public ConsumerFactory<String, KafkaUserRegistrationEvent> consumerFactory() {
//        JsonDeserializer<KafkaUserRegistrationEvent> deserializer =
//                new JsonDeserializer<>(KafkaUserRegistrationEvent.class);
//        deserializer.addTrustedPackages("com.example.hotel_booking_service.kafka.dto");
//        deserializer.setRemoveTypeHeaders(true);
//
//        Map<String, Object> configProps = new HashMap<>();
//        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//
//        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), deserializer);
//    }

    @Bean
    public ConsumerFactory<String, KafkaUserRegistrationEvent> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "hotel_service");
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Явное указание десериализатора с типом
        JsonDeserializer<KafkaUserRegistrationEvent> deserializer = new JsonDeserializer<>(
                KafkaUserRegistrationEvent.class,
                new ObjectMapper()
        );
        deserializer.addTrustedPackages("com.example.hotel_booking_service.kafka.dto");

        return new DefaultKafkaConsumerFactory<>(
                configProps,
                new StringDeserializer(),
                deserializer
        );
    }


    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaUserRegistrationEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KafkaUserRegistrationEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
