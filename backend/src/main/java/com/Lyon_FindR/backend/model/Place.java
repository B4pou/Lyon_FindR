package com.Lyon_FindR.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "places",
        uniqueConstraints = @UniqueConstraint(columnNames = {"osm_type", "osm_id"}))
@Getter
@Setter
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "osm_type", nullable = false)
    private String osmType;          // node, way ou relation

    @Column(name = "osm_id", nullable = false)
    private Long osmId;

    @Column(nullable = false, length = 500)
    private String name;

    @Column(nullable = false)
    private String category;         // ex : "tourism=museum"

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(length = 1000)
    private String website;

    @Column(name = "opening_hours", length = 1000)
    private String openingHours;

    @Column(length = 500)
    private String address;

    @Column(name = "last_imported_at", nullable = false)
    private Instant lastImportedAt;
}
