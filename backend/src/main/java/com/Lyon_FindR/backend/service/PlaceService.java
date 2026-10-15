package com.Lyon_FindR.backend.service;

import lombok.Data;
import com.Lyon_FindR.backend.model.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Lyon_FindR.backend.repository.PlaceRepository;
import java.util.Optional;

@Data
@Service
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;

    public Optional<Place> getPlace(final Long id) {
        return placeRepository.findById(id);
    }

    public Iterable<Place> getPlaces() {
        return placeRepository.findAll();
    }

    public void deletePlace(final Long id) {
        placeRepository.deleteById(id);
    }

    public Place savePlace(Place place) {
        Place savedPlace = placeRepository.save(place);
        return savedPlace;
    }
}
