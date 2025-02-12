package com.example.hotel_booking_service.service;

import com.example.hotel_booking_service.entity.Hotel;
import com.example.hotel_booking_service.mapper.HotelMapper;
import com.example.hotel_booking_service.repository.HotelRepository;
import com.example.hotel_booking_service.web.dto.HotelDto;
import com.example.hotel_booking_service.repository.specification.HotelFilter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с отелями.
 */
@RequiredArgsConstructor
@Service
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final ObjectMapper objectMapper;

    /**
     * Получает все отели с учетом фильтра и пагинации.
     *
     * @param filter фильтр для поиска отелей
     * @param pageable параметры пагинации
     * @return страница с отелями
     */
    public PagedModel<HotelDto> getAllHotels(HotelFilter filter, Pageable pageable) {
        Page<Hotel> hotels = hotelRepository.findAll(filter.toSpecification(), pageable);
        Page<HotelDto> hotelDtos = hotels.map(hotelMapper::toHotelDto);
        return new PagedModel<>(hotelDtos);
    }

    /**
     * Получает один отель по его идентификатору.
     *
     * @param id идентификатор отеля
     * @return DTO отеля
     * @throws EntityNotFoundException если отель не найден
     */
    public HotelDto getOne(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Hotel with id {0} not found", id)));
        return hotelMapper.toHotelDto(hotel);
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
                .collect(Collectors.toList());
    }

    /**
     * Сохраняет новый отель.
     *
     * @param dto DTO нового отеля
     * @return DTO сохраненного отеля
     */
    public HotelDto save(HotelDto dto) {
        Hotel hotel = hotelRepository.save(hotelMapper.toEntity(dto));
        return hotelMapper.toHotelDto(hotel);
    }

    /**
     * Обновляет существующий отель.
     *
     * @param id идентификатор отеля
     * @param dto DTO с обновленными данными
     * @return DTO обновленного отеля
     * @throws EntityNotFoundException если отель не найден
     */
    public HotelDto update(Long id, HotelDto dto) {
        Hotel existingHotel = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Hotel with id {0} not found", id)));
        if (dto.getName() != null) {
            existingHotel.setName(dto.getName());
        }
        if (dto.getTitle() != null) {
            existingHotel.setTitle(dto.getTitle());
        }
        if (dto.getCity() != null) {
            existingHotel.setCity(dto.getCity());
        }
        if (dto.getRating() != null) {
            existingHotel.setRating(dto.getRating());
        }
        if (dto.getNumberofratings() != null) {
            existingHotel.setNumberofratings(dto.getNumberofratings());
        }
        Hotel resultHotel = hotelRepository.save(existingHotel);
        return hotelMapper.toHotelDto(resultHotel);
    }

    /**
     * Частично обновляет существующий отель.
     *
     * @param id идентификатор отеля
     * @param patchNode JSON-объект с обновленными данными
     * @return DTO обновленного отеля
     * @throws EntityNotFoundException если отель не найден
     */
    public HotelDto patch(Long id, JsonNode patchNode) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Hotel with id {0} not found", id)));

        HotelDto hotelDto = hotelMapper.toHotelDto(hotel);
        try {
            objectMapper.readerForUpdating(hotelDto).readValue(patchNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        hotelMapper.updateWithNull(hotelDto, hotel);

        return hotelMapper.toHotelDto(hotelRepository.save(hotel));
    }

    /**
     * Частично обновляет несколько отелей.
     *
     * @param ids список идентификаторов отелей
     * @param patchNode JSON-объект с обновленными данными
     * @return список идентификаторов обновленных отелей
     */
    public List<Long> patchMany(List<Long> ids, JsonNode patchNode) {
        Collection<Hotel> hotels = hotelRepository.findAllById(ids);

        for (Hotel hotel : hotels) {
            HotelDto hotelDto = hotelMapper.toHotelDto(hotel);
            try {
                objectMapper.readerForUpdating(hotelDto).readValue(patchNode);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            hotelMapper.updateWithNull(hotelDto, hotel);
        }

        List<Hotel> resultHotels = hotelRepository.saveAll(hotels);
        return resultHotels.stream()
                .map(Hotel::getId)
                .collect(Collectors.toList());
    }

    /**
     * Удаляет отель по его идентификатору.
     *
     * @param id идентификатор отеля
     * @throws EntityNotFoundException если отель не найден
     */
    public void delete(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Hotel with id {0} not found", id)));
        hotelRepository.delete(hotel);
    }

    /**
     * Удаляет несколько отелей по их идентификаторам.
     *
     * @param ids список идентификаторов отелей
     */
    public void deleteMany(List<Long> ids) {
        List<Hotel> hotels = hotelRepository.findAllById(ids);
        hotelRepository.deleteAll(hotels);
    }
}
