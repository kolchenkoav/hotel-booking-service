package com.example.hotel_booking_service.repository;

import com.example.hotel_booking_service.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Репозиторий для работы с сущностью Room.
 * Наследует интерфейсы JpaRepository и JpaSpecificationExecutor для предоставления стандартных методов CRUD и спецификаций.
 */
public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
}