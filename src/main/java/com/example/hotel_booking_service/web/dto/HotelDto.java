package com.example.hotel_booking_service.web.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Value;

/**
 * DTO для представления информации о гостинице.
 */
@Value
public class HotelDto {
    String name;
    String title;
    String city;
    @PositiveOrZero(message = "The distance from the center should be positive")
    Integer distance;
    Integer rating;
    Integer numberofratings;
}
