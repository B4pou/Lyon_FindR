package com.Lyon_FindR.backend.controller;

import com.Lyon_FindR.backend.model.Place;
import com.Lyon_FindR.backend.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.Lyon_FindR.backend.model.Category;
import jakarta.validation.Valid;
import java.util.List;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    @GetMapping("/places")
    public List<Place> getPlaces(
            @RequestParam(required = false)
            List<Category> categories,

            @RequestParam(defaultValue = "name")
            String sortBy,

            @RequestParam(defaultValue = "ASC")
            String direction) {

        return placeService.getPlaces(
                categories,
                sortBy,
                direction
        );
    }
    @GetMapping("/place/{id}")
    public Place getPlace(@PathVariable("id") final Long id) {
        Optional<Place> place = placeService.getPlace(id);
        if(place.isPresent()) {
            return place.get();
        } else {
            return null;
        }
    }

    @PostMapping("/place")
    public Place createPlace(@Valid @RequestBody Place place) {
        return placeService.savePlace(place);
    }

    @PutMapping("/place/{id}")
    public Place updatePlace(
            @PathVariable Long id,
            @Valid @RequestBody Place place) {

        place.setId(id);
        return placeService.savePlace(place);
    }

    @DeleteMapping("/place/{id}")
    public void deletePlace(@PathVariable("id") final Long id) {
        placeService.deletePlace(id);
    }

}
