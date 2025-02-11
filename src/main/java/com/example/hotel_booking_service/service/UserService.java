package com.example.hotel_booking_service.service;

import com.example.hotel_booking_service.entity.User;
import com.example.hotel_booking_service.mapper.UserMapper;
import com.example.hotel_booking_service.repository.UserRepository;
import com.example.hotel_booking_service.web.dto.UserDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto create(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        User savedUser = userRepository.save(user);
        return new UserDto(savedUser.getUsername(), savedUser.getPassword(), savedUser.getEmail(), savedUser.getRole());
    }

    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(user -> new UserDto(user.getUsername(), user.getPassword(), user.getEmail(), user.getRole()))
                .collect(Collectors.toList());
    }

    public UserDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("User with id {0} not found", id)));
        return userMapper.toUserDto(user);
    }

    public UserDto update(Long id, UserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("User with id {0} not found", id)));

        if (dto.getUsername() != null) {
            user.setUsername(dto.getUsername());
        }
        if (dto.getPassword() != null) {
        user.setPassword(dto.getPassword());
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

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
