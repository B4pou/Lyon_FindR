package com.Lyon_FindR.backend.repository;

import com.Lyon_FindR.backend.model.Place;
import com.Lyon_FindR.backend.model.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    List<Place> findByCategoryIn(
            Collection<Category> categories,
            Sort sort
    );

}
