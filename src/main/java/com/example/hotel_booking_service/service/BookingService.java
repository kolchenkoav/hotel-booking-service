package com.example.hotel_booking_service.service;

import com.example.hotel_booking_service.entity.Booking;
import com.example.hotel_booking_service.entity.Room;
import com.example.hotel_booking_service.entity.User;
import com.example.hotel_booking_service.repository.BookingRepository;
import com.example.hotel_booking_service.repository.RoomRepository;
import com.example.hotel_booking_service.repository.UserRepository;
import com.example.hotel_booking_service.web.dto.BookingRequestDto;
import com.example.hotel_booking_service.web.dto.BookingResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public BookingResponseDto bookRoom(BookingRequestDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));

        boolean isRoomAvailable = bookingRepository.findOverlappingBookings(room, request.getCheckIn(), request.getCheckOut()).isEmpty();
        if (!isRoomAvailable) {
            throw new IllegalStateException("Room is already booked for the selected dates");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setCheckIn(request.getCheckIn());
        booking.setCheckOut(request.getCheckOut());

        Booking savedBooking = bookingRepository.save(booking);
        return toBookingResponseDto(savedBooking);
    }

    public List<BookingResponseDto> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::toBookingResponseDto)
                .collect(Collectors.toList());
    }

    private BookingResponseDto toBookingResponseDto(Booking booking) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setBookingId(booking.getId());
        dto.setUserId(booking.getUser().getId());
        dto.setRoomId(booking.getRoom().getId());
        dto.setCheckIn(booking.getCheckIn());
        dto.setCheckOut(booking.getCheckOut());
        return dto;
    }
}
