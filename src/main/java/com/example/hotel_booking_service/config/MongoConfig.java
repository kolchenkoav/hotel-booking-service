package com.example.hotel_booking_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

//@Configuration
//public class MongoConfig {
//    @Bean
//    public MongoTemplate mongoTemplate() {
//        return new MongoTemplate(MongoClients.create("mongodb://root:mongodb_secret@mongo:27017/mydb"), "mydb");
//    }
//}

@Configuration
@EnableMongoRepositories(basePackages = "com.example.hotel_booking_service.repository")
public class MongoConfig {

    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory() {
        return new SimpleMongoClientDatabaseFactory("mongodb://root:mongodb_secret@localhost:27017/mydb");
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDatabaseFactory());
    }
}

