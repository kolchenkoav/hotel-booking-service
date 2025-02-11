package com.example.hotel_booking_service.web.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

/**
 * DTO for {@link com.example.hotel_booking_service.entity.Room}
 */
@Data
@AllArgsConstructor
public class RoomDto {
    private Long hotelId;
    private String name;
    private String description;
    private String roomNumber;
    @Positive(message = "Цена должна быть положительной")
    private BigDecimal price;
    @Positive
    private Integer maxPeople;
    private Set<LocalDate> unavailableDates;
}