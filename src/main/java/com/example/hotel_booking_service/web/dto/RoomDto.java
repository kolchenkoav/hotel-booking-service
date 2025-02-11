package com.example.hotel_booking_service.web.dto;

import jakarta.validation.constraints.Positive;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

/**
 * DTO for {@link com.example.hotel_booking_service.entity.Room}
 */
@Value
public class RoomDto {
    Long hotelId;
    String name;
    String description;
    String roomNumber;
    @Positive(message = "Цена должна быть положительной")
    BigDecimal price;
    @Positive
    Integer maxPeople;
    Set<LocalDate> unavailableDates;
}