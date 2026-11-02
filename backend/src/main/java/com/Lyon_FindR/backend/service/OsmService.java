package com.Lyon_FindR.backend.service;


import com.Lyon_FindR.backend.client.OverpassClient;
import com.Lyon_FindR.backend.dto.OverpassElement;
import com.Lyon_FindR.backend.dto.OverpassResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;


@Service
public class OsmService {

    private static final Set<String> ALLOWED_KEYS =
            Set.of("tourism", "amenity", "shop", "leisure", "historic", "natural");
    private static final Pattern TAG_VALUE = Pattern.compile("[a-z_]+");

    private final OverpassClient overpassClient;

    private static final Map<String, String> ADMIN_LEVELS = Map.of("Métropole de Lyon", "6");

    public OsmService(OverpassClient overpassClient) {
        this.overpassClient = overpassClient;
    }

    public List<OverpassElement> findPlaces(String city, String key, String value) {
        return search(city, key, value, "out center 50;");
    }

    public List<OverpassElement> findAllPlaces(String city, String key, String value) {
        return search(city, key, value, "out center;");
    }

    private List<OverpassElement> search(String city, String key, String value, String outClause) {
        if (!ALLOWED_KEYS.contains(key) || !TAG_VALUE.matcher(value).matches()) {
            throw new IllegalArgumentException("Catégorie invalide : " + key + "=" + value);
        }
        String safeCity = city.replace("\\", "").replace("\"", "");

        String adminLevel = ADMIN_LEVELS.getOrDefault(safeCity, "8");   // 8 = commune par défaut

        String query = """
        [out:json][timeout:180];
        area["name"="%s"]["boundary"="administrative"]["admin_level"="%s"]->.a;
        nwr["%s"="%s"](area.a);
        %s
        """.formatted(safeCity, adminLevel, key, value, outClause);

        OverpassResponse response = overpassClient.search(query);
        return response == null || response.elements() == null ? List.of() : response.elements();
    }
}
