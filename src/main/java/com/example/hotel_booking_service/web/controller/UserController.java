package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.service.UserService;
import com.example.hotel_booking_service.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления пользователями.
 */
@RestController
@RequestMapping("/rest/admin-ui/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Получает список всех пользователей.
     *
     * @return список пользователей
     */
    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    /**
     * Получает пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return пользователь
     */
    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    /**
     * Создает нового пользователя.
     *
     * @param dto данные нового пользователя
     * @return созданный пользователь
     */
    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(dto));
    }

    /**
     * Обновляет данные пользователя.
     *
     * @param id  идентификатор пользователя
     * @param dto новые данные пользователя
     * @return обновленный пользователь
     */
    @PutMapping("/{id}")
    public UserDto update(@PathVariable Long id, @RequestBody UserDto dto) {
        return userService.update(id, dto);
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return пустой ответ
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
