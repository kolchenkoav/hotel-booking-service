package com.example.hotel_booking_service.mapper;

import com.example.hotel_booking_service.web.dto.UserDto;
import com.example.hotel_booking_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

/**
 * Маппер для преобразования между DTO и сущностью пользователя.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    /**
     * Преобразует DTO пользователя в сущность пользователя.
     *
     * @param userDto DTO пользователя
     * @return сущность пользователя
     */
    User toEntity(UserDto userDto);

    /**
     * Преобразует сущность пользователя в DTO пользователя.
     *
     * @param user сущность пользователя
     * @return DTO пользователя
     */
    UserDto toUserDto(User user);

    /**
     * Обновляет сущность пользователя данными из DTO, игнорируя null значения.
     *
     * @param userDto DTO пользователя
     * @param user сущность пользователя
     * @return обновленная сущность пользователя
     */
    User updateWithNull(UserDto userDto, @MappingTarget User user);
}