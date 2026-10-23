package com.Lyon_FindR.backend.service;

import lombok.Data;
import com.Lyon_FindR.backend.model.Place;
import com.Lyon_FindR.backend.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Lyon_FindR.backend.repository.PlaceRepository;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Sort;

@Data
@Service
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;

    public Optional<Place> getPlace(final Long id) {
        return placeRepository.findById(id);
    }

    public List<Place> getPlaces(
            List<Category> categories,
            String sortBy,
            String direction) {

        if (!List.of("name", "note", "category")
                .contains(sortBy)) {
            throw new IllegalArgumentException(
                    "Critère de tri invalide"
            );
        }

        Sort.Direction sortDirection =
                Sort.Direction.fromString(direction);

        Sort sort = Sort.by(sortDirection, sortBy);

        if (categories == null || categories.isEmpty()) {
            return placeRepository.findAll(sort);
        }

        return placeRepository.findByCategoryIn(categories, sort);
    }

    public void deletePlace(final Long id) {
        placeRepository.deleteById(id);
    }

    public Place savePlace(Place place) {
        Place savedPlace = placeRepository.save(place);
        return savedPlace;
    }
}
