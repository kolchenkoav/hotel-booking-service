package com.example.hotel_booking_service.service;

import com.example.hotel_booking_service.entity.Hotel;
import com.example.hotel_booking_service.entity.Room;
import com.example.hotel_booking_service.mapper.RoomMapper;
import com.example.hotel_booking_service.repository.HotelRepository;
import com.example.hotel_booking_service.repository.RoomRepository;
import com.example.hotel_booking_service.web.dto.RoomDto;
import com.example.hotel_booking_service.web.filter.RoomFilter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final ObjectMapper objectMapper;
    private final HotelRepository hotelRepository;

    public Room create(RoomDto roomDto) {
        Room room = roomMapper.toEntity(roomDto);
        Hotel hotel = hotelRepository.findById(roomDto.getHotelId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel with id `%s` not found".formatted(roomDto.getHotelId())));
        room.setHotel(hotel);
        return roomRepository.save(room);
    }

    public Room edit(Long id, RoomDto roomDto) {
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        Room updatedRoom = roomMapper.toEntity(roomDto);
        updatedRoom.setId(id);
        return roomRepository.save(updatedRoom);
    }

    public Room patch(Long id, JsonNode patchNode) throws IOException {
        Room room = roomRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        RoomDto roomDto = roomMapper.toRoomDto(room);
        objectMapper.readerForUpdating(roomDto).readValue(patchNode);
        roomMapper.updateWithNull(roomDto, room);

        return roomRepository.save(room);
    }

    public List<Long> patchMany(List<Long> ids, JsonNode patchNode) throws IOException {
        Collection<Room> rooms = roomRepository.findAllById(ids);

        for (Room room : rooms) {
            RoomDto roomDto = roomMapper.toRoomDto(room);
            objectMapper.readerForUpdating(roomDto).readValue(patchNode);
            roomMapper.updateWithNull(roomDto, room);
        }

        List<Room> resultRooms = roomRepository.saveAll(rooms);
        return resultRooms.stream()
                .map(Room::getId)
                .collect(Collectors.toList());
    }

    public Room delete(Long id) {
        Room room = roomRepository.findById(id).orElse(null);
        if (room != null) {
            roomRepository.delete(room);
        }
        return room;
    }

    public void deleteMany(List<Long> ids) {
        List<Room> rooms = roomRepository.findAllById(ids);
        roomRepository.deleteAll(rooms);
    }

    public Room getOne(Long id) {
        Optional<Room> roomOptional = roomRepository.findById(id);
        return roomOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    public Page<Room> getAll(RoomFilter filter, Pageable pageable) {
        Specification<Room> spec = filter.toSpecification();
        return roomRepository.findAll(spec, pageable);
    }

    public List<Room> getMany(List<Long> ids) {
        return roomRepository.findAllById(ids);
    }
}