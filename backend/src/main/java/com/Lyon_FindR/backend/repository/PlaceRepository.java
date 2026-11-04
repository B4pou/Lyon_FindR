package com.Lyon_FindR.backend.repository;

import com.Lyon_FindR.backend.model.Place;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    Optional<Place> findByOsmTypeAndOsmId(String osmType, Long osmId);

    List<Place> findByCategoryIn(Collection<String> categories, Sort sort);
}
