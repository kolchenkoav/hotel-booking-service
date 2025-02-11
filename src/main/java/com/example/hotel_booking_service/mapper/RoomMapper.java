package com.example.hotel_booking_service.mapper;

import com.example.hotel_booking_service.entity.Room;
import com.example.hotel_booking_service.web.dto.RoomDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoomMapper {
    @Mapping(source = "hotelId", target = "hotel.id")
    Room toEntity(RoomDto roomDto);

    @Mapping(source = "hotel.id", target = "hotelId")
    RoomDto toRoomDto(Room room);

    @Mapping(source = "hotelId", target = "hotel.id")
    Room updateWithNull(RoomDto roomDto, @MappingTarget Room room);
}