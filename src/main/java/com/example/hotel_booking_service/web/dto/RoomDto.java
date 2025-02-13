package com.example.hotel_booking_service.web.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

/**
 * DTO for {@link com.example.hotel_booking_service.entity.Room}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {
    private Long id;
    private Long hotelId;
    private String name;
    private String description;
    private String roomNumber;
    @Positive(message = "Цена должна быть положительной")
    private BigDecimal price;
    @Positive
    private Integer maxPeople;
    private Set<LocalDate> unavailableDates;
    private LocalDate checkIn;
    private LocalDate checkOut;
}
