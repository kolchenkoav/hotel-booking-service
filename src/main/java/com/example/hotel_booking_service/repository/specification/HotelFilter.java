package com.example.hotel_booking_service.repository.specification;

import com.example.hotel_booking_service.entity.Hotel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Класс, представляющий фильтр для поиска отелей.
 *
 * @param id ID отеля.
 * @param nameStarts Начало имени отеля.
 * @param titleStarts Начало заголовка отеля.
 * @param cityStarts Начало города, в котором находится отель.
 * @param addressStarts Начало адреса отеля.
 * @param distanceLte Максимальное расстояние до отеля.
 * @param ratingIn Список рейтингов отеля.
 * @param numberofratings Количество отзывов о отеле.
 */
public record HotelFilter(
        Long id,
        String nameStarts,
        String titleStarts,
        String cityStarts,
        String addressStarts,
        Integer distanceLte,
        List<Integer> ratingIn,
        Integer numberofratings
) {

    public Specification<Hotel> toSpecification() {
        return Specification.where(idSpec())
                .and(hotelnameStartsSpec())
                .and(titleStartsSpec())
                .and(cityStartsSpec())
                .and(addressStartsSpec())
                .and(distanceLteSpec())
                .and(ratingInSpec())
                .and(numberOfRatingsSpec());
    }

    private Specification<Hotel> idSpec() {
        return ((root, query, cb) -> id != null
                ? cb.lessThanOrEqualTo(root.get("id"), id)
                : null);
    }

    private Specification<Hotel> hotelnameStartsSpec() {
        return ((root, query, cb) -> StringUtils.hasText(nameStarts)
                ? cb.like(cb.lower(root.get("name")), nameStarts.toLowerCase() + "%")
                : null);
    }

    private Specification<Hotel> titleStartsSpec() {
        return ((root, query, cb) -> StringUtils.hasText(titleStarts)
                ? cb.like(cb.lower(root.get("title")), titleStarts.toLowerCase() + "%")
                : null);
    }

    private Specification<Hotel> cityStartsSpec() {
        return ((root, query, cb) -> StringUtils.hasText(cityStarts)
                ? cb.like(cb.lower(root.get("city")), cityStarts.toLowerCase() + "%")
                : null);
    }

    private Specification<Hotel> addressStartsSpec() {
        return ((root, query, cb) -> StringUtils.hasText(addressStarts)
                ? cb.like(cb.lower(root.get("address")), addressStarts.toLowerCase() + "%")
                : null);
    }

    private Specification<Hotel> distanceLteSpec() {
        return ((root, query, cb) -> distanceLte != null
                ? cb.lessThanOrEqualTo(root.get("distance"), distanceLte)
                : null);
    }

    private Specification<Hotel> ratingInSpec() {
        return ((root, query, cb) -> ratingIn != null && !ratingIn.isEmpty()
                ? root.get("rating").in(ratingIn)
                : null);
    }

    private Specification<Hotel> numberOfRatingsSpec() {
        return ((root, query, cb) -> numberofratings != null
                ? cb.equal(root.get("numberofratings"), numberofratings)
                : null);
    }
}
