package com.Lyon_FindR.backend.service;

import com.Lyon_FindR.backend.dto.ImportReport;
import com.Lyon_FindR.backend.dto.OverpassElement;
import com.Lyon_FindR.backend.model.Place;
import com.Lyon_FindR.backend.repository.PlaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ImportService {

    private static final Logger log = LoggerFactory.getLogger(ImportService.class);
    private static final long PAUSE_MS = 5000;

    private record Category(String key, String value) {
        String label() { return key + "=" + value; }
    }

    // Commence avec une ou deux catégories pour tester, tu agrandiras ensuite
    private static final List<Category> CATEGORIES = List.of(
            new Category("tourism", "museum"),
            new Category("tourism", "gallery")
    );

    private final OsmService osmService;
    private final PlaceRepository placeRepository;

    public ImportService(OsmService osmService, PlaceRepository placeRepository) {
        this.osmService = osmService;
        this.placeRepository = placeRepository;
    }

    public ImportReport importCity(String city) {
        List<ImportReport.CategoryResult> results = new ArrayList<>();

        for (int i = 0; i < CATEGORIES.size(); i++) {
            Category c = CATEGORIES.get(i);
            try {
                results.add(importCategory(city, c));
            } catch (RestClientException e) {
                log.warn("Import {} en échec : {}", c.label(), e.getMessage());
                results.add(new ImportReport.CategoryResult(c.label(), 0, 0, 0, e.getMessage()));
            }
            if (i < CATEGORIES.size() - 1) {
                pause();
            }
        }
        return new ImportReport(results);
    }

    private ImportReport.CategoryResult importCategory(String city, Category c) {
        List<OverpassElement> elements = osmService.findAllPlaces(city, c.key(), c.value());
        List<Place> toSave = new ArrayList<>();
        int created = 0, updated = 0, skipped = 0;

        for (OverpassElement e : elements) {
            Map<String, String> tags = e.tags();
            String name = tags == null ? null : tags.get("name");
            Double lat = e.lat() != null ? e.lat() : (e.center() != null ? e.center().lat() : null);
            Double lon = e.lon() != null ? e.lon() : (e.center() != null ? e.center().lon() : null);

            if (name == null || name.isBlank() || lat == null || lon == null) {
                skipped++;
                continue;
            }

            Place place = placeRepository.findByOsmTypeAndOsmId(e.type(), e.id()).orElse(null);
            if (place == null) {
                place = new Place();
                place.setOsmType(e.type());
                place.setOsmId(e.id());
                created++;
            } else {
                updated++;
            }

            place.setName(name);
            place.setCategory(c.label());
            place.setLatitude(lat);
            place.setLongitude(lon);
            place.setWebsite(tags.get("website"));
            place.setOpeningHours(tags.get("opening_hours"));
            place.setAddress(buildAddress(tags));
            place.setLastImportedAt(Instant.now());
            toSave.add(place);
        }

        placeRepository.saveAll(toSave);
        return new ImportReport.CategoryResult(c.label(), created, updated, skipped, null);
    }

    private String buildAddress(Map<String, String> tags) {
        String street = tags.get("addr:street");
        if (street == null) return null;
        String number = tags.get("addr:housenumber");
        return number == null ? street : number + " " + street;
    }

    private void pause() {
        try {
            Thread.sleep(PAUSE_MS);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
