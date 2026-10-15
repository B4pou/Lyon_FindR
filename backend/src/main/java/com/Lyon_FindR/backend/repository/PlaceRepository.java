package com.Lyon_FindR.backend.repository;

import com.Lyon_FindR.backend.model.Place;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends CrudRepository<Place, Long> {

}
