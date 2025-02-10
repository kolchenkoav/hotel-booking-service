package com.example.hotel_booking_service.mapper;

import com.example.hotel_booking_service.web.dto.HotelDto;
import com.example.hotel_booking_service.entity.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

/**
 * Маппер для преобразования между DTO и сущностью отеля.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface HotelMapper {

    /**
     * Преобразует DTO отеля в сущность отеля.
     *
     * @param hotelDto DTO отеля
     * @return сущность отеля
     */
    Hotel toEntity(HotelDto hotelDto);

    /**
     * Преобразует сущность отеля в DTO отеля.
     *
     * @param hotel сущность отеля
     * @return DTO отеля
     */
    HotelDto toHotelDto(Hotel hotel);

    /**
     * Обновляет сущность отеля данными из DTO, игнорируя null значения.
     *
     * @param hotelDto DTO отеля
     * @param hotel сущность отеля
     * @return обновленная сущность отеля
     */
    Hotel updateWithNull(HotelDto hotelDto, @MappingTarget Hotel hotel);
}