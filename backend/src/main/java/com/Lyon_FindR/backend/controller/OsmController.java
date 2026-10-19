package com.Lyon_FindR.backend.controller;

import com.Lyon_FindR.backend.dto.OverpassElement;
import com.Lyon_FindR.backend.service.OsmService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.util.List;

@RestController
@RequestMapping("/api/osm")
public class OsmController {

    private final OsmService osmService;

    public OsmController(OsmService osmService) {
        this.osmService = osmService;
    }

    @GetMapping("/places")
    public List<OverpassElement> places(@RequestParam(defaultValue = "Lyon") String city,
                                        @RequestParam(defaultValue = "tourism") String key,
                                        @RequestParam(defaultValue = "museum") String value) {
        return osmService.findPlaces(city, key, value);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String handleBadRequest(IllegalArgumentException e) {
        return e.getMessage();
    }

    @ExceptionHandler(org.springframework.web.client.RestClientException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    String handleOsmUnavailable(RestClientException e) {
        return "Le service OpenStreetMap est indisponible, réessaie plus tard.";
    }
}
