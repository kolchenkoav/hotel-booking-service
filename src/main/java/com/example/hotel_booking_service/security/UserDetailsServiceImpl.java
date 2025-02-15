package com.example.hotel_booking_service.security;

import com.example.hotel_booking_service.entity.User;
import com.example.hotel_booking_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Реализация сервиса для загрузки данных пользователя.
 * Этот сервис используется для получения данных пользователя по имени пользователя.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Загружает данные пользователя по имени пользователя.
     *
     * @param username имя пользователя
     * @return данные пользователя
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return new UserDetailsImpl(user);
    }
}
