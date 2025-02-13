package com.example.hotel_booking_service.service;

import com.example.hotel_booking_service.entity.User;
import com.example.hotel_booking_service.kafka.KafkaProducerService;
import com.example.hotel_booking_service.kafka.dto.KafkaUserRegistrationEvent;
import com.example.hotel_booking_service.mapper.UserMapper;
import com.example.hotel_booking_service.repository.UserRepository;
import com.example.hotel_booking_service.web.dto.UserDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления пользователями.
 */
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final KafkaProducerService kafkaProducerService;

    /**
     * Получает список всех пользователей.
     *
     * @return список DTO пользователей
     */
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(user -> new UserDto(user.getUsername(), user.getPassword(), user.getEmail(), user.getRole()))
                .collect(Collectors.toList());
    }

    /**
     * Получает пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return DTO пользователя
     * @throws EntityNotFoundException если пользователь не найден
     */
    public UserDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.
                        format("User with id {0} not found", id)));
        return userMapper.toUserDto(user);
    }

    /**
     * Создает нового пользователя.
     *
     * @param dto DTO нового пользователя
     * @return DTO созданного пользователя
     */
    public UserDto create(UserDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        User savedUser = userRepository.save(user);

        // Отправляем событие в Kafka
        KafkaUserRegistrationEvent event = new KafkaUserRegistrationEvent();
        event.setUserId(savedUser.getId());
        kafkaProducerService.sendUserRegistration(event);

        return userMapper.toUserDto(savedUser);
    }

    /**
     * Обновляет существующего пользователя.
     *
     * @param id идентификатор пользователя
     * @param dto DTO с обновленными данными пользователя
     * @return DTO обновленного пользователя
     * @throws EntityNotFoundException если пользователь не найден
     */
    public UserDto update(Long id, UserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("User with id {0} not found", id)));

        if (dto.getUsername() != null) {
            user.setUsername(dto.getUsername());
        }
        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }

        User savedUser = userRepository.save(user);
        return userMapper.toUserDto(savedUser);
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     */
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
