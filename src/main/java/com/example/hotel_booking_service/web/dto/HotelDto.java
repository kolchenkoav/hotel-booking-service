package com.example.hotel_booking_service.web.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * DTO для представления информации о гостинице.
 */
@Data
@AllArgsConstructor
public class HotelDto {
    private Long id;
    private String name;
    private String title;
    private String city;
    private String address;
    @PositiveOrZero(message = "The distance from the center should be positive")
    private Integer distance;
    private Integer rating;
    private Integer numberofratings;
    private Integer newMark;
}
