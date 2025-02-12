package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.service.RoomService;
import com.example.hotel_booking_service.web.dto.RoomDto;
import com.example.hotel_booking_service.repository.specification.RoomFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/admin-ui/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public PagedModel<RoomDto> getAll(@ModelAttribute RoomFilter filter, Pageable pageable) {
        return roomService.getAll(filter, pageable);
    }

    @GetMapping("/{id}")
    public RoomDto getOne(@PathVariable Long id) {
        return roomService.getOne(id);
    }

    @GetMapping("/by-ids")
    public List<RoomDto> getMany(@RequestParam List<Long> ids) {
        return roomService.getMany(ids);
    }

    @PostMapping
    public ResponseEntity<RoomDto> createNewRoom(@RequestBody RoomDto roomDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.save(roomDto));
    }

    @PutMapping("/{id}")
    public RoomDto update(@PathVariable Long id, @RequestBody RoomDto roomDto) {
        return roomService.update(id, roomDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/by-ids")
    public ResponseEntity<Void> deleteMany(@RequestParam List<Long> ids) {
        roomService.deleteMany(ids);
        return ResponseEntity.noContent().build();
    }
}
