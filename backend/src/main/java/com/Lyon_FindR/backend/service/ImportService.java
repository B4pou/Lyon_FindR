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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class ImportService {

    private static final Logger log = LoggerFactory.getLogger(ImportService.class);
    private static final long PAUSE_MS = 5000;

    private record Category(String key, String value) {
        String label() { return key + "=" + value; }
    }

    private static final List<Category> CATEGORIES = Stream.of(

        categories("tourism",
                "aquarium", "artwork", "attraction", "gallery", "guest_house", "hostel",
                "hotel", "information", "motel", "museum", "picnic_site", "theme_park",
                "viewpoint", "zoo"),

        categories("amenity",
                // Manger et boire
                "bar", "biergarten", "cafe", "fast_food", "food_court", "ice_cream", "pub",
                "restaurant",
                // Éducation
                "college", "dancing_school", "kindergarten", "language_school", "library",
                "toy_library", "training", "music_school", "school", "traffic_park",
                "university",
                // Transport
                "bicycle_rental", "bicycle_wash", "bus_station", "fuel", "taxi",
                // "bicycle_parking", "motorcycle_parking", "parking",   // très nombreux, rarement nommés
                "bank",
                // Culture et sorties
                "arts_centre", "casino", "cinema", "community_center", "events_venue",
                "exhibition_center", "fountain", "music_venue", "nightclub", "planetarium",
                "stage", "theatre", "townhall",
                // Équipements
                "bbq", "watering_point",
                // "bench", "shelter", "clock",                          // très nombreux, rarement nommés
                "place_of_worship", "public_bath"),

        categories("shop",
                "bakery", "beverages", "brewing_supplies", "butcher", "cheese", "chocolate",
                "coffee", "confectionery", "convenience", "food", "ice_cream", "pastry", "wine",
                "kiosk", "shoes", "hairdresser", "massage", "piercing", "tattoo",
                "games", "model", "music", "video", "video_games", "anime",
                "books", "clothes"),

        categories("leisure",
                "adult_gaming_centre", "amusement_arcade", "bandstand", "bathing_place",
                "beach_resort", "bowling_alley", "dance", "disc_golf_course", "dog_park",
                "escape_game", "firepit", "fishing", "fitness_centre", "fitness_station",
                "garden", "golf_course", "hackerspace", "high_ropes_course", "horse_riding",
                "ice_rink", "marina", "miniature_golf", "nature_reserve", "park",
                "playground", "resort", "sauna", "sports_centre", "sports_hall", "stadium",
                "sunbathing", "swimming_area", "swimming_pool", "track", "trampoline_park",
                "water_park"
                // "outdoor_seating", "picnic_table", "pitch",           // très nombreux, rarement nommés
        ),

        categories("historic",
                "aircraft", "anchor", "aqueduct", "archaeological_site", "battlefield",
                "bomb_crater", "building", "castle", "castle_wall", "church",
                "city_gate", "citywalls", "fort", "high_cross", "locomotive", "machine",
                "manor", "memorial", "millstone", "monastery", "monument", "mosque",
                "pillory", "round_tower", "ruins", "rune_stone", "temple", "tomb", "tower"
                // "boundary_stone"                                      // bornes : rarement nommées
        ),

        categories("natural",
                "bay", "beach", "geyser", "hot_spring", "spring", "arch", "cave_entrance",
                "cliff", "gorge", "hill", "rock", "stone")

    ).flatMap(List::stream).toList();

    private static final Map<String, String> DEFAULT_NAMES = Map.ofEntries(
            // tourism
            Map.entry("tourism=artwork", "Œuvre d'art"),
            Map.entry("tourism=picnic_site", "Aire de pique-nique"),
            Map.entry("tourism=viewpoint", "Point de vue"),

            // amenity
            Map.entry("amenity=bbq", "Barbecue"),
            Map.entry("amenity=fountain", "Fontaine"),
            Map.entry("amenity=watering_point", "Point d'eau"),
            Map.entry("amenity=bicycle_wash", "Lavage de vélos"),
            Map.entry("amenity=bicycle_rental", "Station de vélos"),
            Map.entry("amenity=taxi", "Station de taxis"),
            Map.entry("amenity=fuel", "Station-service"),
            Map.entry("amenity=public_bath", "Bains publics"),
            Map.entry("amenity=place_of_worship", "Lieu de culte"),
            Map.entry("amenity=stage", "Scène"),

            // shop
            Map.entry("shop=kiosk", "Kiosque"),

            // leisure
            Map.entry("leisure=park", "Parc"),
            Map.entry("leisure=playground", "Aire de jeux"),
            Map.entry("leisure=dog_park", "Parc à chiens"),
            Map.entry("leisure=fitness_station", "Agrès de fitness"),
            Map.entry("leisure=firepit", "Foyer"),
            Map.entry("leisure=bandstand", "Kiosque à musique"),
            Map.entry("leisure=bathing_place", "Lieu de baignade"),
            Map.entry("leisure=swimming_area", "Zone de baignade"),
            Map.entry("leisure=fishing", "Zone de pêche"),
            Map.entry("leisure=miniature_golf", "Minigolf"),
            Map.entry("leisure=ice_rink", "Patinoire"),

            // historic
            Map.entry("historic=monument", "Monument"),
            Map.entry("historic=ruins", "Ruines"),
            Map.entry("historic=tomb", "Tombeau"),
            Map.entry("historic=tower", "Tour"),
            Map.entry("historic=city_gate", "Porte de ville"),
            Map.entry("historic=citywalls", "Remparts"),
            Map.entry("historic=castle_wall", "Mur d'enceinte"),
            Map.entry("historic=aqueduct", "Aqueduc"),
            Map.entry("historic=archaeological_site", "Site archéologique"),
            Map.entry("historic=church", "Église"),
            Map.entry("historic=temple", "Temple"),
            Map.entry("historic=mosque", "Mosquée"),
            Map.entry("historic=monastery", "Monastère"),
            Map.entry("historic=manor", "Manoir"),
            Map.entry("historic=fort", "Fort"),
            Map.entry("historic=building", "Bâtiment historique"),

            // natural
            Map.entry("natural=beach", "Plage"),
            Map.entry("natural=spring", "Source"),
            Map.entry("natural=hot_spring", "Source chaude"),
            Map.entry("natural=cave_entrance", "Entrée de grotte"),
            Map.entry("natural=arch", "Arche naturelle"),
            Map.entry("natural=gorge", "Gorge"),
            Map.entry("natural=bay", "Baie"),
            Map.entry("natural=hill", "Colline")
    );

    private static List<Category> categories(String key, String... values) {
        return Arrays.stream(values).map(v -> new Category(key, v)).toList();
    }

    private final OsmService osmService;
    private final PlaceRepository placeRepository;

    public ImportService(OsmService osmService, PlaceRepository placeRepository) {
        this.osmService = osmService;
        this.placeRepository = placeRepository;
    }

    public ImportReport importCity(String city, String key, String value) {
        List<Category> selected;
        if (key != null && value != null) {
            selected = List.of(new Category(key, value));
        } else {
            selected = CATEGORIES.stream()
                    .filter(c -> key == null || c.key().equals(key))
                    .filter(c -> value == null || c.value().equals(value))
                    .toList();
        }

        List<ImportReport.CategoryResult> results = new ArrayList<>();

        for (int i = 0; i < selected.size(); i++) {
            Category c = selected.get(i);
            try {
                results.add(importCategory(city, c));
            } catch (RestClientException e) {
                log.warn("Import {} en échec : {}", c.label(), e.getMessage());
                results.add(new ImportReport.CategoryResult(c.label(), 0, 0, 0, e.getMessage()));
            }
            if (i < selected.size() - 1) {
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
            if (name == null || name.isBlank()) {
                name = DEFAULT_NAMES.get(c.label());   // null si la catégorie n'est pas dans la table
            }
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

        log.info("Import {} : {} créés, {} mis à jour, {} ignorés", c.label(), created, updated, skipped);
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
