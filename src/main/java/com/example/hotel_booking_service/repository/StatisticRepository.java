package com.example.hotel_booking_service.repository;

import com.example.hotel_booking_service.entity.Statistic;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatisticRepository extends MongoRepository<Statistic, String> {
}
