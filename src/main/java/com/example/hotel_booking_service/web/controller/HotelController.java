package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.service.HotelService;
import com.example.hotel_booking_service.web.dto.HotelDto;
import com.example.hotel_booking_service.repository.specification.HotelFilter;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления отелями.
 */
@RestController
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    /**
     * Обновляет рейтинг отеля.
     *
     * @param id идентификатор отеля
     * @param newMark новый рейтинг отеля
     * @return обновленная информация об отеле
     */
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{id}/rating")
    public HotelDto updateRating(@PathVariable Long id, @RequestParam Integer newMark) {
        return hotelService.updateRating(id, newMark);
    }

    /**
     * Получает список отелей с возможностью фильтрации и пагинации.
     *
     * @param filter фильтр для поиска отелей
     * @param pageable параметры пагинации
     * @return список отелей
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public PagedModel<HotelDto> getAllHotels(@ModelAttribute HotelFilter filter, Pageable pageable) {
        return hotelService.getAllHotels(filter, pageable);
    }

    /**
     * Получает информацию об одном отеле по его идентификатору.
     *
     * @param id идентификатор отеля
     * @return информация об отеле
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public HotelDto getOne(@PathVariable Long id) {
        return hotelService.getOne(id);
    }

    /**
     * Получает информацию о нескольких отелях по их идентификаторам.
     *
     * @param ids список идентификаторов отелей
     * @return список информации об отелях
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-ids")
    public List<HotelDto> getMany(@RequestParam List<Long> ids) {
        return hotelService.getMany(ids);
    }

    /**
     * Создает новый отель.
     *
     * @param dto данные нового отеля
     * @return информация о созданном отеле
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<HotelDto> create(@RequestBody @Valid HotelDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.save(dto));
    }

    /**
     * Обновляет информацию об отеле.
     *
     * @param id идентификатор отеля
     * @param dto новые данные отеля
     * @return обновленная информация об отеле
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public HotelDto update(@PathVariable Long id, @RequestBody @Valid HotelDto dto) {
        return hotelService.update(id, dto);
    }

    /**
     * Частично обновляет информацию об отеле.
     *
     * @param id идентификатор отеля
     * @param patchNode данные для частичного обновления
     * @return обновленная информация об отеле
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public HotelDto patch(@PathVariable Long id, @RequestBody JsonNode patchNode) {
        return hotelService.patch(id, patchNode);
    }

    /**
     * Частично обновляет информацию о нескольких отелях.
     *
     * @param ids список идентификаторов отелей
     * @param patchNode данные для частичного обновления
     * @return список идентификаторов обновленных отелей
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/by-ids")
    public List<Long> patchMany(@RequestParam @Valid List<Long> ids, @RequestBody JsonNode patchNode) {
        return hotelService.patchMany(ids, patchNode);
    }

    /**
     * Удаляет отель по его идентификатору.
     *
     * @param id идентификатор отеля
     * @return статус ответа
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        hotelService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Удаляет несколько отелей по их идентификаторам.
     *
     * @param ids список идентификаторов отелей
     * @return статус ответа
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/by-ids")
    public ResponseEntity<Void> deleteMany(@RequestParam List<Long> ids) {
        hotelService.deleteMany(ids);
        return ResponseEntity.noContent().build();
    }
}
