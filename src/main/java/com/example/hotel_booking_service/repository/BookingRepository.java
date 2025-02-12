package com.example.hotel_booking_service.repository;

import com.example.hotel_booking_service.entity.Booking;
import com.example.hotel_booking_service.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByRoomId(Long roomId);

    @Query("SELECT b FROM Booking b WHERE b.room = :room AND " +
            "(b.checkIn < :checkOut AND b.checkOut > :checkIn)")
    List<Booking> findOverlappingBookings(Room room, LocalDate checkIn, LocalDate checkOut);
}
