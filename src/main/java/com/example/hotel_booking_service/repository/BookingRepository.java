package com.example.hotel_booking_service.repository;

import com.example.hotel_booking_service.entity.Booking;
import com.example.hotel_booking_service.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

/**
 * Репозиторий для работы с бронированиями.
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Находит все бронирования для указанного номера.
     *
     * @param roomId идентификатор номера
     * @return список бронирований
     */
    List<Booking> findByRoomId(Long roomId);

    /**
     * Находит все пересекающиеся бронирования для указанного номера и дат.
     *
     * @param room номер
     * @param checkIn дата заезда
     * @param checkOut дата выезда
     * @return список пересекающихся бронирований
     */
    @Query("SELECT b FROM Booking b WHERE b.room = :room AND " +
            "(b.checkIn < :checkOut AND b.checkOut > :checkIn)")
    List<Booking> findOverlappingBookings(Room room, LocalDate checkIn, LocalDate checkOut);
}
