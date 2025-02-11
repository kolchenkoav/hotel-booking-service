package com.example.hotel_booking_service.web.filter;

import com.example.hotel_booking_service.entity.Room;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record RoomFilter(String nameStarts, String descriptionStarts, String roomNumberContains, BigDecimal priceGte,
                         BigDecimal priceLte, Integer maxPeople) {
    public Specification<Room> toSpecification() {
        return Specification.where(nameStartsSpec())
                .and(descriptionStartsSpec())
                .and(roomNumberContainsSpec())
                .and(priceGteSpec())
                .and(priceLteSpec())
                .and(maxPeopleSpec());
    }

    private Specification<Room> nameStartsSpec() {
        return ((root, query, cb) -> StringUtils.hasText(nameStarts)
                ? cb.like(cb.lower(root.get("name")), nameStarts.toLowerCase() + "%")
                : null);
    }

    private Specification<Room> descriptionStartsSpec() {
        return ((root, query, cb) -> StringUtils.hasText(descriptionStarts)
                ? cb.like(cb.lower(root.get("description")), descriptionStarts.toLowerCase() + "%")
                : null);
    }

    private Specification<Room> roomNumberContainsSpec() {
        return ((root, query, cb) -> StringUtils.hasText(roomNumberContains)
                ? cb.like(root.get("roomNumber"), "%" + roomNumberContains + "%")
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
}
