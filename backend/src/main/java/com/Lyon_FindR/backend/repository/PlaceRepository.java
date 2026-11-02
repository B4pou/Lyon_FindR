package com.Lyon_FindR.backend.repository;

import com.Lyon_FindR.backend.model.Place;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PlaceRepository extends CrudRepository<Place, Long> {
    Optional<Place> findByOsmTypeAndOsmId(String osmType, Long osmId);
}
