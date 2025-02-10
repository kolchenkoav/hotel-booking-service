package com.example.hotel_booking_service.repository;

import com.example.hotel_booking_service.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Репозиторий для работы с сущностями отелей.
 * Наследует интерфейсы JpaRepository и JpaSpecificationExecutor для предоставления стандартных методов CRUD и спецификаций.
 */
public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {
}