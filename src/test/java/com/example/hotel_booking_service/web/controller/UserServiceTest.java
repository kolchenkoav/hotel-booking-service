package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.entity.RoleType;
import com.example.hotel_booking_service.entity.User;
import com.example.hotel_booking_service.kafka.KafkaProducerService;
import com.example.hotel_booking_service.kafka.dto.KafkaUserRegistrationEvent;
import com.example.hotel_booking_service.repository.UserRepository;
import com.example.hotel_booking_service.service.UserService;
import com.example.hotel_booking_service.web.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private KafkaProducerService kafkaProducerService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        kafkaProducerService = mock(KafkaProducerService.class);
        userService = new UserService(userRepository, null, passwordEncoder, kafkaProducerService);
    }

    @Test
    void shouldRegisterUserAndSendKafkaEvent() {
        UserDto userDto = new UserDto("testuser", "password123", "testuser@example.com", RoleType.ROLE_USER);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername(userDto.getUsername());
        savedUser.setPassword(userDto.getPassword());
        savedUser.setEmail(userDto.getEmail());
        savedUser.setRole(userDto.getRole());

        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");

        UserDto result = userService.create(userDto);

        assertThat(result.getUsername()).isEqualTo("testuser");
        verify(userRepository, times(1)).save(any(User.class));

        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(kafkaProducerService, times(1)).sendUserRegistration(any());
    }

    @Test
    void testUserRegistration() {
        UserDto userDto = new UserDto("testuser", "password123", "testuser@example.com", RoleType.ROLE_USER);

        // Mock repository behavior
        User savedUser = new User();
        savedUser.setId(1L); // Simulate ID generation
        savedUser.setUsername(userDto.getUsername());
        savedUser.setPassword(userDto.getPassword());
        savedUser.setEmail(userDto.getEmail());
        savedUser.setRole(userDto.getRole());

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Call service method
        UserDto result = userService.create(userDto);

        // Verify results
        assertThat(result.getUsername()).isEqualTo("testuser");
        verify(userRepository, times(1)).save(any(User.class));

        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(kafkaProducerService, times(1)).sendUserRegistration(new
                KafkaUserRegistrationEvent());
        assertThat(userIdCaptor.getValue()).isEqualTo(1L); // Ensure userId is set
    }
}

