package com.example.hotel_booking_service.service;

import com.example.hotel_booking_service.entity.Hotel;
import com.example.hotel_booking_service.mapper.HotelMapper;
import com.example.hotel_booking_service.repository.HotelRepository;
import com.example.hotel_booking_service.web.dto.HotelDto;
import com.example.hotel_booking_service.web.filter.HotelFilter;
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
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final ObjectMapper objectMapper;

    public Hotel create(HotelDto dto) {
        Hotel hotel = hotelMapper.toEntity(dto);
        return hotelRepository.save(hotel);
    }

    public Hotel edit(Long id, HotelDto dto) {
        Hotel existingHotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        Hotel updatedHotel = hotelMapper.toEntity(dto);
        updatedHotel.setId(id);
        return hotelRepository.save(updatedHotel);
    }

    public Hotel patch(Long id, JsonNode patchNode) throws IOException {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

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

    public Hotel delete(Long id) {
        Hotel hotel = hotelRepository.findById(id).orElse(null);
        if (hotel != null) {
            hotelRepository.delete(hotel);
        }
        return hotel;
    }

    public void deleteMany(List<Long> ids) {
        List<Hotel> hotels = hotelRepository.findAllById(ids);
        hotelRepository.deleteAll(hotels);
    }

    public Hotel getOne(Long id) {
        Optional<Hotel> hotelOptional = hotelRepository.findById(id);
        return hotelOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    public Page<Hotel> getAll(HotelFilter filter, Pageable pageable) {
        Specification<Hotel> spec = filter.toSpecification();
        return hotelRepository.findAll(spec, pageable);
    }

    public List<Hotel> getMany(List<Long> ids) {
        return hotelRepository.findAllById(ids);
    }
}