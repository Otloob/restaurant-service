package com.outloob.restaurant.search.repository;

import com.outloob.restaurant.restaurant.entity.Restaurant;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RestaurantNearbyQueryRepositoryImpl implements RestaurantNearbyQueryRepository {

    private static final String SPATIAL_WHERE = """
            FROM restaurant r
            WHERE r.status = :status
              AND ST_DWithin(
                    ST_SetSRID(ST_MakePoint(r.longitude, r.latitude), 4326)::geography,
                    ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography,
                    :radiusMeters
              )
              AND (:restaurantName = '' OR LOWER(r.name) LIKE LOWER(CONCAT('%', :restaurantName, '%')))
              AND (:categoryName = '' OR EXISTS (
                    SELECT 1 FROM menu_category c
                    WHERE c.restaurant_id = r.id
                      AND LOWER(c.name) LIKE LOWER(CONCAT('%', :categoryName, '%'))
              ))
              AND ((:dishName = '' AND :tag = '') OR EXISTS (
                    SELECT 1 FROM menu_category c
                    JOIN menu_item i ON i.category_id = c.id
                    WHERE c.restaurant_id = r.id
                      AND (:dishName = '' OR LOWER(i.name) LIKE LOWER(CONCAT('%', :dishName, '%')))
                      AND (:tag = '' OR LOWER(i.tag) LIKE LOWER(CONCAT('%', :tag, '%')))
              ))
            """;

    private final EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Page<Restaurant> findNearby(String status, double latitude, double longitude,
                                       double radiusMeters, String restaurantName, String categoryName,
                                       String dishName, String tag, Pageable pageable) {
        Session session = entityManager.unwrap(Session.class);
        NativeQuery<Restaurant> resultsQuery = session.createNativeQuery(
                "SELECT r.* " + SPATIAL_WHERE + """
                        ORDER BY ST_Distance(
                            ST_SetSRID(ST_MakePoint(r.longitude, r.latitude), 4326)::geography,
                            ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography
                        ), r.id
                        """,
                Restaurant.class);
        bind(resultsQuery, status, latitude, longitude, radiusMeters,
                restaurantName, categoryName, dishName, tag);
        resultsQuery.setFirstResult(Math.toIntExact(pageable.getOffset()));
        resultsQuery.setMaxResults(pageable.getPageSize());
        List<Restaurant> results = resultsQuery.getResultList();

        NativeQuery<?> countQuery = session.createNativeQuery("SELECT COUNT(*) " + SPATIAL_WHERE);
        bind(countQuery, status, latitude, longitude, radiusMeters,
                restaurantName, categoryName, dishName, tag);
        long total = ((Number) countQuery.getSingleResult()).longValue();
        return new PageImpl<>(results, pageable, total);
    }

    private void bind(NativeQuery<?> query, String status, double latitude, double longitude, double radiusMeters,
                      String restaurantName, String categoryName, String dishName, String tag) {
        query.setParameter("status", status);
        query.setParameter("latitude", latitude);
        query.setParameter("longitude", longitude);
        query.setParameter("radiusMeters", radiusMeters);
        query.setParameter("restaurantName", valueOrEmpty(restaurantName));
        query.setParameter("categoryName", valueOrEmpty(categoryName));
        query.setParameter("dishName", valueOrEmpty(dishName));
        query.setParameter("tag", valueOrEmpty(tag));
    }

    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }
}
