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
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

/**
 * Сервис для управления комнатами отеля.
 */
@RequiredArgsConstructor
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final HotelRepository hotelRepository;

    /**
     * Получает список комнат с учетом фильтрации и постраничной навигации.
     *
     * @param filter   фильтр для комнат
     * @param pageable параметры постраничной навигации
     * @return страница с DTO комнат
     */
    public PagedModel<RoomDto> getAll(RoomFilter filter, Pageable pageable) {
        Specification<Room> spec = filter.toSpecification();
        Page<Room> rooms = roomRepository.findAll(spec, pageable);
        Page<RoomDto> roomDtos = rooms.map(roomMapper::toRoomDto);

        return new PagedModel<>(roomDtos);
    }

    /**
     * Получает одну комнату по идентификатору.
     *
     * @param id идентификатор комнаты
     * @return DTO комнаты
     * @throws EntityNotFoundException если комната не найдена
     */
    public RoomDto getOne(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Room with id {0} not found", id)));
        return roomMapper.toRoomDto(room);
    }

    /**
     * Получает список комнат по списку идентификаторов.
     *
     * @param ids список идентификаторов комнат
     * @return список DTO комнат
     */
    public List<RoomDto> getMany(List<Long> ids) {
        return roomRepository.findAllById(ids).stream()
                .map(roomMapper::toRoomDto).toList();
    }

    /**
     * Создает новую комнату.
     *
     * @param roomDto DTO новой комнаты
     * @return DTO созданной комнаты
     * @throws EntityNotFoundException если отель не найден
     */
    public RoomDto save(RoomDto roomDto) {
        Room room = roomMapper.toEntity(roomDto);
        Hotel hotel = hotelRepository.findById(roomDto.getHotelId())
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Hotel with id {0} not found", roomDto.getHotelId())));
        room.setHotel(hotel);
        return roomMapper.toRoomDto(roomRepository.save(room));
    }

    /**
     * Обновляет существующую комнату.
     *
     * @param id      идентификатор комнаты
     * @param roomDto DTO с обновленными данными
     * @return DTO обновленной комнаты
     * @throws EntityNotFoundException если комната или отель не найдены
     */
    public RoomDto update(Long id, RoomDto roomDto) {
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Room with id {0} not found", id)));
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
                    .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                            .format("Hotel with id {0} not found", roomDto.getHotelId())));
            existingRoom.setHotel(hotel);
        }

        return roomMapper.toRoomDto(roomRepository.save(existingRoom));
    }

    /**
     * Удаляет комнату по идентификатору.
     *
     * @param id идентификатор комнаты
     * @throws EntityNotFoundException если комната не найдена
     */
    public void delete(Long id) {
        roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Room with id {0} not found", id)));
        roomRepository.deleteById(id);
    }

    /**
     * Удаляет несколько комнат по списку идентификаторов.
     *
     * @param ids список идентификаторов комнат
     */
    public void deleteMany(List<Long> ids) {
        List<Room> rooms = roomRepository.findAllById(ids);
        roomRepository.deleteAll(rooms);
    }
}
