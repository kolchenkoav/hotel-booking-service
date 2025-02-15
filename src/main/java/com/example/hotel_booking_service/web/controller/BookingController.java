package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.service.BookingService;
import com.example.hotel_booking_service.web.dto.BookingRequestDto;
import com.example.hotel_booking_service.web.dto.BookingResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления бронированиями номеров в отеле.
 */
@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    /**
     * Создает новое бронирование номера.
     *
     * @param request объект запроса для бронирования номера
     * @return объект ответа с информацией о созданном бронировании
     */
    @PostMapping
    public BookingResponseDto bookRoom(@RequestBody @Valid BookingRequestDto request) {
        return bookingService.bookRoom(request);
    }

    /**
     * Получает список всех бронирований.
     *
     * @return список объектов ответа с информацией о всех бронированиях
     */
    @GetMapping
    public List<BookingResponseDto> getAllBookings() {
        return bookingService.getAllBookings();
    }
}
