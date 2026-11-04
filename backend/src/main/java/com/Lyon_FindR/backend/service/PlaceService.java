package com.Lyon_FindR.backend.service;

import com.Lyon_FindR.backend.model.Place;
import com.Lyon_FindR.backend.repository.PlaceRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PlaceService {

    private static final Set<String> SORTABLE_FIELDS = Set.of("name", "category");

    private PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public Optional<Place> getPlace(final String type, final Long id) { // osm_type : way, node, relation
        return placeRepository.findByOsmTypeAndOsmId(type, id);
    }

    public List<Place> getPlaces(List<String> categories, String sortBy, String direction) {
        if (!SORTABLE_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException("Critère de tri invalide : " + sortBy);
        }

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);

        if (categories == null || categories.isEmpty()) {
            return placeRepository.findAll(sort);
        }
        return placeRepository.findByCategoryIn(categories, sort);
    }

    public void deletePlace(final Long id) {
        placeRepository.deleteById(id);
    }

    public Place savePlace(Place place) {
        return placeRepository.save(place);
    }
}
