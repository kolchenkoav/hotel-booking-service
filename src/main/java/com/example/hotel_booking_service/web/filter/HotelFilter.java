package com.example.hotel_booking_service.web.filter;

import com.example.hotel_booking_service.entity.Hotel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Класс фильтра для отелей.
 */
public record HotelFilter(String nameStarts, String titleStarts, String cityStarts, Integer distanceLte,
                          List<Integer> ratingIn) {

    /**
     * Преобразует фильтр в спецификацию JPA.
     *
     * @return спецификация JPA
     */
    public Specification<Hotel> toSpecification() {
        return Specification.where(hotelnameStartsSpec())
                .and(titleStartsSpec())
                .and(cityStartsSpec())
                .and(distanceLteSpec())
                .and(ratingInSpec());
    }

    /**
     * Спецификация для фильтрации по началу имени отеля.
     *
     * @return спецификация JPA
     */
    private Specification<Hotel> hotelnameStartsSpec() {
        return ((root, query, cb) -> StringUtils.hasText(nameStarts)
                ? cb.like(cb.lower(root.get("name")), nameStarts.toLowerCase() + "%")
                : null);
    }

    /**
     * Спецификация для фильтрации по началу заголовка отеля.
     *
     * @return спецификация JPA
     */
    private Specification<Hotel> titleStartsSpec() {
        return ((root, query, cb) -> StringUtils.hasText(titleStarts)
                ? cb.like(cb.lower(root.get("title")), titleStarts.toLowerCase() + "%")
                : null);
    }

    /**
     * Спецификация для фильтрации по началу города отеля.
     *
     * @return спецификация JPA
     */
    private Specification<Hotel> cityStartsSpec() {
        return ((root, query, cb) -> StringUtils.hasText(cityStarts)
                ? cb.like(cb.lower(root.get("city")), cityStarts.toLowerCase() + "%")
                : null);
    }

    /**
     * Спецификация для фильтрации по максимальному расстоянию до отеля.
     *
     * @return спецификация JPA
     */
    private Specification<Hotel> distanceLteSpec() {
        return ((root, query, cb) -> distanceLte != null
                ? cb.lessThanOrEqualTo(root.get("distance"), distanceLte)
                : null);
    }

    /**
     * Спецификация для фильтрации по рейтингу отеля.
     *
     * @return спецификация JPA
     */
    private Specification<Hotel> ratingInSpec() {
        return ((root, query, cb) -> ratingIn != null
                ? root.get("rating").in(ratingIn)
                : null);
    }
}