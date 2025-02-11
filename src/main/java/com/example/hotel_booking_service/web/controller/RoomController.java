package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.entity.Room;
import com.example.hotel_booking_service.mapper.RoomMapper;
import com.example.hotel_booking_service.service.RoomService;
import com.example.hotel_booking_service.web.dto.RoomDto;
import com.example.hotel_booking_service.web.filter.RoomFilter;
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
    public ResponseEntity<RoomDto> getOne(@PathVariable Long id) {
        Room room = roomService.getOne(id);
        return ResponseEntity.ok(roomMapper.toRoomDto(room));
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
    public ResponseEntity<RoomDto> edit(@PathVariable Long id, @RequestBody RoomDto roomDto) {
        Room updatedRoom = roomService.edit(id, roomDto);
        return ResponseEntity.ok(roomMapper.toRoomDto(updatedRoom));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RoomDto> patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        Room patchedRoom = roomService.patch(id, patchNode);
        return ResponseEntity.ok(roomMapper.toRoomDto(patchedRoom));
    }

    @PatchMapping
    public ResponseEntity<List<Long>> patchMany(@RequestParam List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        List<Long> patchedRoomIds = roomService.patchMany(ids, patchNode);
        return ResponseEntity.ok(patchedRoomIds);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RoomDto> delete(@PathVariable Long id) {
        Room deletedRoom = roomService.delete(id);
        if (deletedRoom != null) {
            return ResponseEntity.ok(roomMapper.toRoomDto(deletedRoom));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMany(@RequestParam List<Long> ids) {
        roomService.deleteMany(ids);
        return ResponseEntity.noContent().build();
    }
}
