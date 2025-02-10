package com.example.hotel_booking_service.service;

import com.example.hotel_booking_service.web.dto.HotelDto;
import com.example.hotel_booking_service.web.filter.HotelFilter;
import com.example.hotel_booking_service.entity.Hotel;
import com.example.hotel_booking_service.mapper.HotelMapper;
import com.example.hotel_booking_service.repository.HotelRepository;
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

@RequiredArgsConstructor
@Service
public class HotelService {

    private final HotelMapper hotelMapper;

    private final HotelRepository hotelRepository;

    private final ObjectMapper objectMapper;

    /**
     * Получает все отели с учетом фильтра и пагинации.
     *
     * @param filter фильтр для поиска отелей
     * @param pageable параметры пагинации
     * @return страница с отелями
     */
    public Page<HotelDto> getAll(HotelFilter filter, Pageable pageable) {
        Specification<Hotel> spec = filter.toSpecification();
        Page<Hotel> hotels = hotelRepository.findAll(spec, pageable);
        return hotels.map(hotelMapper::toHotelDto);
    }

    /**
     * Получает один отель по его идентификатору.
     *
     * @param id идентификатор отеля
     * @return DTO отеля
     * @throws ResponseStatusException если отель не найден
     */
    public HotelDto getOne(Long id) {
        Optional<Hotel> hotelOptional = hotelRepository.findById(id);
        return hotelMapper.toHotelDto(hotelOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    /**
     * Получает несколько отелей по их идентификаторам.
     *
     * @param ids список идентификаторов отелей
     * @return список DTO отелей
     */
    public List<HotelDto> getMany(List<Long> ids) {
        List<Hotel> hotels = hotelRepository.findAllById(ids);
        return hotels.stream()
                .map(hotelMapper::toHotelDto)
                .toList();
    }

    /**
     * Создает новый отель.
     *
     * @param dto DTO нового отеля
     * @return DTO созданного отеля
     */
    public HotelDto create(HotelDto dto) {
        Hotel hotel = hotelMapper.toEntity(dto);
        Hotel resultHotel = hotelRepository.save(hotel);
        return hotelMapper.toHotelDto(resultHotel);
    }

    /**
     * Редактирует существующий отель.
     *
     * @param id идентификатор отеля
     * @param dto DTO с обновленными данными отеля
     * @return DTO обновленного отеля
     * @throws ResponseStatusException если отель не найден
     */
    public HotelDto edit(Long id, HotelDto dto) {
        Hotel existingHotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        Hotel updatedHotel = hotelMapper.toEntity(dto);
        updatedHotel.setId(id);
        Hotel resultHotel = hotelRepository.save(updatedHotel);
        return hotelMapper.toHotelDto(resultHotel);
    }

    /**
     * Частично обновляет существующий отель.
     *
     * @param id идентификатор отеля
     * @param patchNode JSON-объект с обновленными данными
     * @return DTO обновленного отеля
     * @throws IOException если произошла ошибка при чтении JSON
     * @throws ResponseStatusException если отель не найден
     */
    public HotelDto patch(Long id, JsonNode patchNode) throws IOException {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        HotelDto hotelDto = hotelMapper.toHotelDto(hotel);
        objectMapper.readerForUpdating(hotelDto).readValue(patchNode);
        hotelMapper.updateWithNull(hotelDto, hotel);

        Hotel resultHotel = hotelRepository.save(hotel);
        return hotelMapper.toHotelDto(resultHotel);
    }

    /**
     * Частично обновляет несколько отелей.
     *
     * @param ids список идентификаторов отелей
     * @param patchNode JSON-объект с обновленными данными
     * @return список идентификаторов обновленных отелей
     * @throws IOException если произошла ошибка при чтении JSON
     */
    public List<Long> patchMany(List<Long> ids, JsonNode patchNode) throws IOException {
        Collection<Hotel> hotels = hotelRepository.findAllById(ids);

        for (Hotel hotel : hotels) {
            HotelDto hotelDto = hotelMapper.toHotelDto(hotel);
            objectMapper.readerForUpdating(hotelDto).readValue(patchNode);
            hotelMapper.updateWithNull(hotelDto, hotel);
        }

        List<Hotel> resultHotels = hotelRepository.saveAll(hotels);
        return resultHotels.stream()
                .map(Hotel::getId)
                .toList();
    }

    /**
     * Удаляет отель по его идентификатору.
     *
     * @param id идентификатор отеля
     * @return DTO удаленного отеля
     */
    public HotelDto delete(Long id) {
        Hotel hotel = hotelRepository.findById(id).orElse(null);
        if (hotel != null) {
            hotelRepository.delete(hotel);
        }
        return hotelMapper.toHotelDto(hotel);
    }

    /**
     * Удаляет несколько отелей по их идентификаторам.
     *
     * @param ids список идентификаторов отелей
     */
    public void deleteMany(List<Long> ids) {
        hotelRepository.deleteAllById(ids);
    }
}
