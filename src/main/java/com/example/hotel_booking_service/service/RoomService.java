package com.example.hotel_booking_service.service;

import com.example.hotel_booking_service.entity.Hotel;
import com.example.hotel_booking_service.entity.Room;
import com.example.hotel_booking_service.mapper.RoomMapper;
import com.example.hotel_booking_service.repository.HotelRepository;
import com.example.hotel_booking_service.repository.RoomRepository;
import com.example.hotel_booking_service.web.dto.RoomDto;
import com.example.hotel_booking_service.repository.specification.RoomFilter;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final HotelRepository hotelRepository;

    public Room create(RoomDto roomDto) {
        Room room = roomMapper.toEntity(roomDto);
        Hotel hotel = hotelRepository.findById(roomDto.getHotelId())
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Hotel with id {0} not found", roomDto.getHotelId())));
        room.setHotel(hotel);
        return roomRepository.save(room);
    }

    public RoomDto update(Long id, RoomDto roomDto) {
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Room with id {0} not found", id)));
        if (roomDto.getName() != null) {
            existingRoom.setName(roomDto.getName());
        }
        if (roomDto.getDescription() != null) {
            existingRoom.setDescription(roomDto.getDescription());
        }
        if (roomDto.getRoomNumber() != null) {
            existingRoom.setRoomNumber(roomDto.getRoomNumber());
        }
        if (roomDto.getPrice() != null) {
            existingRoom.setPrice(roomDto.getPrice());
        }
        if (roomDto.getMaxPeople() != null) {
            existingRoom.setMaxPeople(roomDto.getMaxPeople());
        }
        if (roomDto.getUnavailableDates() != null) {
            existingRoom.setUnavailableDates(roomDto.getUnavailableDates());
        }
        if (roomDto.getHotelId() != null) {
            Hotel hotel = hotelRepository.findById(roomDto.getHotelId())
                    .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Hotel with id {0} not found", roomDto.getHotelId())));
            existingRoom.setHotel(hotel);
        }

        return roomMapper.toRoomDto(roomRepository.save(existingRoom));
    }

    public RoomDto delete(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Room with id {0} not found", id)));
        return roomMapper.toRoomDto(room);
    }

    public void deleteMany(List<Long> ids) {
        List<Room> rooms = roomRepository.findAllById(ids);
        roomRepository.deleteAll(rooms);
    }

    public RoomDto getOne(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Room with id {0} not found", id)));
        return roomMapper.toRoomDto(room);

    }

    public Page<Room> getAll(RoomFilter filter, Pageable pageable) {
        Specification<Room> spec = filter.toSpecification();
        return roomRepository.findAll(spec, pageable);
    }

    public List<Room> getMany(List<Long> ids) {
        return roomRepository.findAllById(ids);
    }
}
