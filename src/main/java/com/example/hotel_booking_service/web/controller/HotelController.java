package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.entity.Hotel;
import com.example.hotel_booking_service.mapper.HotelMapper;
import com.example.hotel_booking_service.service.HotelService;
import com.example.hotel_booking_service.web.dto.HotelDto;
import com.example.hotel_booking_service.repository.specification.HotelFilter;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/admin-ui/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;
    private final HotelMapper hotelMapper;

    @GetMapping
    public PagedModel<HotelDto> getAll(@ModelAttribute HotelFilter filter, Pageable pageable) {
        return hotelService.getAll(filter, pageable);
    }

    @PostMapping
    public HotelDto create(@RequestBody @Valid HotelDto dto) {
        return hotelService.create(dto);
    }

    @PutMapping("/{id}")
    public HotelDto update(@PathVariable Long id, @RequestBody @Valid HotelDto dto) {
        return hotelService.update(id, dto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HotelDto> patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        Hotel patchedHotel = hotelService.patch(id, patchNode);
        return ResponseEntity.ok(hotelMapper.toHotelDto(patchedHotel));
    }

    @DeleteMapping("/{id}")
    public HotelDto delete(@PathVariable Long id) {
        return hotelService.delete(id);
    }

    @GetMapping("/by-ids")
    public ResponseEntity<List<HotelDto>> getMany(@RequestParam List<Long> ids) {
        List<Hotel> hotels = hotelService.getMany(ids);
        List<HotelDto> hotelDtos = hotels.stream()
                .map(hotelMapper::toHotelDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(hotelDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDto> getOne(@PathVariable Long id) {
        Hotel hotel = hotelService.getOne(id);
        return ResponseEntity.ok(hotelMapper.toHotelDto(hotel));
    }

    @PatchMapping
    public ResponseEntity<List<Long>> patchMany(@RequestParam @Valid List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        List<Long> patchedIds = hotelService.patchMany(ids, patchNode);
        return ResponseEntity.ok(patchedIds);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMany(@RequestParam List<Long> ids) {
        hotelService.deleteMany(ids);
        return ResponseEntity.noContent().build();
    }
}