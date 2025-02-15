package com.example.hotel_booking_service.repository;

import com.example.hotel_booking_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с пользователями.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Найти пользователя по имени пользователя.
     *
     * @param username имя пользователя
     * @return пользователь или null, если не найден
     */
    User findByUsername(String username);

    /**
     * Найти пользователя по email.
     *
     * @param email email пользователя
     * @return пользователь или null, если не найден
     */
    User findByEmail(String email);

    /**
     * Проверить, существует ли пользователь с таким именем пользователя.
     *
     * @param username имя пользователя
     * @return true, если существует, иначе false
     */
    boolean existsByUsername(String username);

    /**
     * Проверить, существует ли пользователь с таким email.
     *
     * @param email email пользователя
     * @return true, если существует, иначе false
     */
    boolean existsByEmail(String email);
}