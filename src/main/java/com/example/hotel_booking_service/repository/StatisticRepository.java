package com.example.hotel_booking_service.repository;

import com.example.hotel_booking_service.entity.Statistic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

@EnableMongoRepositories
@Repository
public interface StatisticRepository extends MongoRepository<Statistic, String> {
}
