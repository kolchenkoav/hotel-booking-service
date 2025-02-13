package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.entity.Booking;
import com.example.hotel_booking_service.entity.Room;
import com.example.hotel_booking_service.entity.User;
import com.example.hotel_booking_service.kafka.KafkaProducerService;
import com.example.hotel_booking_service.repository.BookingRepository;
import com.example.hotel_booking_service.repository.RoomRepository;
import com.example.hotel_booking_service.repository.UserRepository;
import com.example.hotel_booking_service.service.BookingService;
import com.example.hotel_booking_service.web.dto.BookingRequestDto;
import com.example.hotel_booking_service.web.dto.BookingResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BookingServiceTest {
    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private RoomRepository roomRepository;
    private KafkaProducerService kafkaProducerService;
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingRepository = mock(BookingRepository.class);
        userRepository = mock(UserRepository.class);
        roomRepository = mock(RoomRepository.class);
        kafkaProducerService = mock(KafkaProducerService.class);
        bookingService = new BookingService(bookingRepository, userRepository, roomRepository, kafkaProducerService);
    }

    @Test
    void shouldBookRoomAndSendKafkaEvent() {
        BookingRequestDto request = new BookingRequestDto();
        request.setUserId(1L);
        request.setRoomId(1L);
        request.setCheckIn(LocalDate.now().plusDays(1));
        request.setCheckOut(LocalDate.now().plusDays(3));

        User user = new User();
        user.setId(1L);

        Room room = new Room();
        room.setId(1L);

        Booking savedBooking = new Booking();
        savedBooking.setId(1L);
        savedBooking.setUser(user);
        savedBooking.setRoom(room);
        savedBooking.setCheckIn(request.getCheckIn());
        savedBooking.setCheckOut(request.getCheckOut());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(bookingRepository.findOverlappingBookings(room, request.getCheckIn(), request.getCheckOut()))
                .thenReturn(java.util.Collections.emptyList());
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

        BookingResponseDto result = bookingService.bookRoom(request);

        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getRoomId()).isEqualTo(1L);
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verify(kafkaProducerService, times(1)).sendRoomBooking(any());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        BookingRequestDto request = new BookingRequestDto();
        request.setUserId(1L);
        request.setRoomId(1L);
        request.setCheckIn(LocalDate.now().plusDays(1));
        request.setCheckOut(LocalDate.now().plusDays(3));

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.bookRoom(request));
    }
}
