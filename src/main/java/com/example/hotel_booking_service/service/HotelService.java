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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final ObjectMapper objectMapper;

    public PagedModel<HotelDto> getAll(HotelFilter filter, Pageable pageable) {
        Page<Hotel> hotels = hotelRepository.findAll(filter.toSpecification(), pageable);
        Page<HotelDto> hotelDtos = hotels.map(hotelMapper::toHotelDto);
        return new PagedModel<>(hotelDtos);
    }

    public HotelDto create(HotelDto dto) {
        Hotel hotel = hotelRepository.save(hotelMapper.toEntity(dto));
        return hotelMapper.toHotelDto(hotel);
    }

    public HotelDto update(Long id, HotelDto dto) {
        Hotel existingHotel = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Hotel with id {0} not found", id)));
        if (dto.getName() != null) {
            existingHotel.setName(dto.getName());
        }
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

    public Hotel patch(Long id, JsonNode patchNode) throws IOException {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Hotel with id {0} not found", id)));

        HotelDto hotelDto = hotelMapper.toHotelDto(hotel);
        objectMapper.readerForUpdating(hotelDto).readValue(patchNode);
        hotelMapper.updateWithNull(hotelDto, hotel);

        return hotelRepository.save(hotel);
    }

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
                .collect(Collectors.toList());
    }

    public HotelDto delete(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Hotel with id {0} not found", id)));
        hotelRepository.delete(hotel);
        return hotelMapper.toHotelDto(hotel);
    }

    public void deleteMany(List<Long> ids) {
        List<Hotel> hotels = hotelRepository.findAllById(ids);
        hotelRepository.deleteAll(hotels);
    }

    public Hotel getOne(Long id) {
        Optional<Hotel> hotelOptional = hotelRepository.findById(id);
        return hotelOptional
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Hotel with id {0} not found", id)));
    }



    public List<Hotel> getMany(List<Long> ids) {
        return hotelRepository.findAllById(ids);
    }
}