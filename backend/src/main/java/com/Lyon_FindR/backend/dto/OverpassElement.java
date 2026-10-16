package com.Lyon_FindR.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OverpassElement(long id, double lat, double lon, Map<String, String> tags) {}
