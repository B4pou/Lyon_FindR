package com.Lyon_FindR.backend.repository;

import com.Lyon_FindR.backend.model.Favorite;
import com.Lyon_FindR.backend.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    boolean existsByUserIdAndPlaceId(Long userId, Long placeId);

    void deleteByUserIdAndPlaceId(Long userId, Long placeId);

    @Query("select f.place from Favorite f where f.userId = :userId order by f.addedAt desc")
    List<Place> findFavoritePlacesByUserId(@Param("userId") Long userId);
}
