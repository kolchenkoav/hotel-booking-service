package com.example.hotel_booking_service.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Data
@Document(collection = "statistics")
public class Statistic {
    @Id
    private String id;
    private Long userId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String eventType; // "REGISTRATION" или "BOOKING"
}
