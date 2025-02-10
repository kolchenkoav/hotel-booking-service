package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.web.dto.HotelDto;
import com.example.hotel_booking_service.web.filter.HotelFilter;
import com.example.hotel_booking_service.service.HotelService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

/**
 * Контроллер для управления отелями.
 */
@RestController
@RequestMapping("/rest/admin-ui/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    /**
     * Получает список отелей с возможностью фильтрации и пагинации.
     *
     * @param filter фильтр для отелей
     * @param pageable параметры пагинации
     * @return страница с отелями
     */
    @GetMapping
    public ResponseEntity<PagedModel<HotelDto>> getAll(@ModelAttribute HotelFilter filter, Pageable pageable) {
        Page<HotelDto> hotelDtos = hotelService.getAll(filter, pageable);
        PagedModel<HotelDto> pagedModel = new PagedModel<>(hotelDtos);
        return ResponseEntity.ok(pagedModel);
    }

    /**
     * Получает информацию об одном отеле по его идентификатору.
     *
     * @param id идентификатор отеля
     * @return информация об отеле
     */
    @GetMapping("/{id}")
    public ResponseEntity<HotelDto> getOne(@PathVariable Long id) {
        HotelDto hotelDto = hotelService.getOne(id);
        if (hotelDto != null) {
            return ResponseEntity.ok(hotelDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Получает информацию о нескольких отелях по их идентификаторам.
     *
     * @param ids список идентификаторов отелей
     * @return список информации об отелях
     */
    @GetMapping("/by-ids")
    public ResponseEntity<List<HotelDto>> getMany(@RequestParam List<Long> ids) {
        List<HotelDto> hotelDtos = hotelService.getMany(ids);
        return ResponseEntity.ok(hotelDtos);
    }

    /**
     * Создает новый отель.
     *
     * @param dto данные нового отеля
     * @return информация о созданном отеле
     */
    @PostMapping
    public ResponseEntity<HotelDto> create(@RequestBody @Valid HotelDto dto) {
        HotelDto createdHotelDto = hotelService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHotelDto);
    }

    /**
     * Частично обновляет информацию об отеле.
     *
     * @param id идентификатор отеля
     * @param patchNode данные для обновления
     * @return обновленная информация об отеле
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @PatchMapping("/{id}")
    public ResponseEntity<HotelDto> patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        HotelDto patchedHotelDto = hotelService.patch(id, patchNode);
        if (patchedHotelDto != null) {
            return ResponseEntity.ok(patchedHotelDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Частично обновляет информацию о нескольких отелях.
     *
     * @param ids список идентификаторов отелей
     * @param patchNode данные для обновления
     * @return список идентификаторов обновленных отелей
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @PatchMapping
    public ResponseEntity<List<Long>> patchMany(@RequestParam @Valid List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        List<Long> patchedIds = hotelService.patchMany(ids, patchNode);
        return ResponseEntity.ok(patchedIds);
    }

    /**
     * Удаляет отель по его идентификатору.
     *
     * @param id идентификатор отеля
     * @return информация об удаленном отеле
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HotelDto> delete(@PathVariable Long id) {
        HotelDto deletedHotelDto = hotelService.delete(id);
        if (deletedHotelDto != null) {
            return ResponseEntity.ok(deletedHotelDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Удаляет несколько отелей по их идентификаторам.
     *
     * @param ids список идентификаторов отелей
     * @return пустой ответ
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteMany(@RequestParam List<Long> ids) {
        hotelService.deleteMany(ids);
        return ResponseEntity.noContent().build();
    }
}
