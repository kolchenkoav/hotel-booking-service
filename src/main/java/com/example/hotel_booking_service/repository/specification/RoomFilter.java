package com.example.hotel_booking_service.repository.specification;

import com.example.hotel_booking_service.entity.Room;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;


public record RoomFilter(Long id, String nameStarts,
                         BigDecimal priceGte,
                         BigDecimal priceLte, Integer maxPeople, LocalDate checkIn, LocalDate checkOut, Long hotelId) {
    //private static LocalDate checkIn;
    //private static LocalDate checkOut;

    public Specification<Room> toSpecification() {
        return Specification.where(idSpec())        // ID комнаты
                .and(nameStartsSpec())              // Название комнаты начинается с
                .and(priceGteSpec())                // Цена минимальная
                .and(priceLteSpec())                // Цена максимальная
                .and(maxPeopleSpec())               // количество гостей в комнате
                .and(unavailableDatesSpec())        // Дата заезда и дата выезда
                .and(hotelIdSpec());                // ID отеля
    }

    private Specification<Room> idSpec() {
        return ((root, query, cb) -> id != null
                ? cb.equal(root.get("id"), id)
                : null);
    }

    private Specification<Room> nameStartsSpec() {
        return ((root, query, cb) -> StringUtils.hasText(nameStarts)
                ? cb.like(cb.lower(root.get("name")), nameStarts.toLowerCase() + "%")
                : null);
    }

    private Specification<Room> priceGteSpec() {
        return ((root, query, cb) -> priceGte != null
                ? cb.greaterThanOrEqualTo(root.get("price"), priceGte)
                : null);
    }

    private Specification<Room> priceLteSpec() {
        return ((root, query, cb) -> priceLte != null
                ? cb.lessThanOrEqualTo(root.get("price"), priceLte)
                : null);
    }

    private Specification<Room> maxPeopleSpec() {
        return ((root, query, cb) -> maxPeople != null
                ? cb.equal(root.get("maxPeople"), maxPeople)
                : null);
    }

    private Specification<Room> unavailableDatesSpec() {
        return (root, query, cb) -> {
            if (checkIn != null && checkOut != null) {
                Subquery<LocalDate> subquery = query != null ? query.subquery(LocalDate.class) : null;
                assert subquery != null;
                Root<Room> subRoot = subquery.from(Room.class);
                subquery.select(subRoot.get("unavailableDates"));
                subquery.where(cb.and(
                        cb.greaterThanOrEqualTo(subRoot.get("unavailableDates"), checkIn),
                        cb.lessThanOrEqualTo(subRoot.get("unavailableDates"), checkOut)
                ));
                return cb.not(cb.exists(subquery));
            }
            return null;
        };
    }

    private Specification<Room> hotelIdSpec() {
        return ((root, query, cb) -> hotelId != null
                ? cb.equal(root.get("hotelId"), hotelId)
                : null);
    }
}
