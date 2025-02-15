package com.example.hotel_booking_service.repository;

import com.example.hotel_booking_service.entity.Statistic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с сущностью Statistic в MongoDB.
 * Наследует интерфейс MongoRepository, предоставляющий стандартные методы для работы с MongoDB.
 */
@Repository
public interface StatisticRepository extends MongoRepository<Statistic, String> {
}
