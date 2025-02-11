package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.entity.Room;
import com.example.hotel_booking_service.mapper.RoomMapper;
import com.example.hotel_booking_service.service.RoomService;
import com.example.hotel_booking_service.web.dto.RoomDto;
import com.example.hotel_booking_service.repository.specification.RoomFilter;
import com.fasterxml.jackson.databind.JsonNode;
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
@RequestMapping("/rest/admin-ui/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RoomMapper roomMapper;

    @PostMapping
    public ResponseEntity<RoomDto> create(@RequestBody RoomDto roomDto) {
        Room createdRoom = roomService.create(roomDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomMapper.toRoomDto(createdRoom));
    }

    @GetMapping
    public ResponseEntity<PagedModel<RoomDto>> getAll(@ModelAttribute RoomFilter filter, Pageable pageable) {
        Page<Room> rooms = roomService.getAll(filter, pageable);
        Page<RoomDto> roomDtos = rooms.map(roomMapper::toRoomDto);
        PagedModel<RoomDto> pagedModel = new PagedModel<>(roomDtos);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public RoomDto getOne(@PathVariable Long id) {
        return roomService.getOne(id);
    }

    @GetMapping("/by-ids")
    public ResponseEntity<List<RoomDto>> getMany(@RequestParam List<Long> ids) {
        List<Room> rooms = roomService.getMany(ids);
        List<RoomDto> roomDtos = rooms.stream()
                .map(roomMapper::toRoomDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roomDtos);
    }

    @PutMapping("/{id}")
    public RoomDto update(@PathVariable Long id, @RequestBody RoomDto roomDto) {
        return roomService.update(id, roomDto);
    }

    @DeleteMapping("/{id}")
    public RoomDto delete(@PathVariable Long id) {
        return roomService.delete(id);
    }
//    public ResponseEntity<RoomDto> delete(@PathVariable Long id) {
//        Room deletedRoom = roomService.delete(id);
//        if (deletedRoom != null) {
//            return ResponseEntity.ok(roomMapper.toRoomDto(deletedRoom));
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMany(@RequestParam List<Long> ids) {
        roomService.deleteMany(ids);
        return ResponseEntity.noContent().build();
    }
}
