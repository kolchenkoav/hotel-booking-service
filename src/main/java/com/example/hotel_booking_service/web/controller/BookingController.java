package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.service.BookingService;
import com.example.hotel_booking_service.web.dto.BookingRequestDto;
import com.example.hotel_booking_service.web.dto.BookingResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/admin-ui/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto bookRoom(@RequestBody @Valid BookingRequestDto request) {
        return bookingService.bookRoom(request);
    }

    @GetMapping
    public List<BookingResponseDto> getAllBookings() {
        return bookingService.getAllBookings();
    }
}
