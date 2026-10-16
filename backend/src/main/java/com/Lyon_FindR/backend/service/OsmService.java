package com.Lyon_FindR.backend.service;


import com.Lyon_FindR.backend.client.OverpassClient;
import com.Lyon_FindR.backend.dto.OverpassElement;
import com.Lyon_FindR.backend.dto.OverpassResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;


@Service
public class OsmService {

    private static final Pattern TAG_VALUE = Pattern.compile("[a-z_]+");

    private final OverpassClient overpassClient;

    public OsmService(OverpassClient overpassClient) {
        this.overpassClient = overpassClient;
    }

    public List<OverpassElement> findPlaces(String city, String tourism) {
        if (!TAG_VALUE.matcher(tourism).matches()) {
            throw new IllegalArgumentException("Catégorie invalide : " + tourism);
        }
        String safeCity = city.replace("\\", "").replace("\"", "");

        //  dmin_level=8 pour les commune française
        String query = """
                [out:json][timeout:25];
                area["name"="%s"]["boundary"="administrative"]["admin_level"="8"]->.a;  
                node["tourism"="%s"](area.a);
                out body 50;
                """.formatted(safeCity, tourism);

        OverpassResponse response = overpassClient.search(query);
        return response == null || response.elements() == null ? List.of() : response.elements();
    }
}
