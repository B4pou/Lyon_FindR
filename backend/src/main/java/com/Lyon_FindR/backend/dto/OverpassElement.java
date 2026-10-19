package com.Lyon_FindR.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OverpassElement(String type, long id, Double lat, Double lon,
                              Center center, Map<String, String> tags) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Center(double lat, double lon) {}
}
