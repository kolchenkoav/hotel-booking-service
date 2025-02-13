package com.example.hotel_booking_service.kafka;

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

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        JsonDeserializer<Object> deserializer = new JsonDeserializer<>();
        deserializer.addTrustedPackages("com.example.hotel_booking_service.kafka.dto");
        deserializer.setRemoveTypeHeaders(false);

        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);

        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), deserializer);
    }

//    @Bean
//    public ConsumerFactory<String, KafkaUserRegistrationEvent> consumerFactory() {
//        JsonDeserializer<KafkaUserRegistrationEvent> deserializer = new JsonDeserializer<>(KafkaUserRegistrationEvent.class);
//        deserializer.addTrustedPackages("com.example.hotel_booking_service.kafka.dto");
//        deserializer.setRemoveTypeHeaders(false);  // Говорит Kafka сохранять заголовки __TypeId__
//
//        Map<String, Object> configProps = new HashMap<>();
//        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
//
//        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), deserializer);
//    }

//    @Bean
//    public KafkaTemplate<String, Object> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }
//
//    // Consumer с JSON десериализацией и ErrorHandling
//    @Bean
//    public ConsumerFactory<String, Object> consumerFactory() {
//        Map<String, Object> configProps = new HashMap<>();
//        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "hotel_service");
//
////        JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<>(Object.class);
////        jsonDeserializer.addTrustedPackages("*"); // Доверяем все пакеты
////        jsonDeserializer.setRemoveTypeHeaders(false);
////        jsonDeserializer.setUseTypeMapperForKey(false);
//        JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<>();
//        jsonDeserializer.addTrustedPackages("com.example.hotel_booking_service.kafka.dto");
//        jsonDeserializer.setRemoveTypeHeaders(false);
//        jsonDeserializer.setTypeMapper(new DefaultKafkaTypeMapper()); // Установка TypeMapper
//
//
//        return new DefaultKafkaConsumerFactory<>(
//                configProps,
//                new StringDeserializer(),
//                new ErrorHandlingDeserializer<>(jsonDeserializer)
//        );
//    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
