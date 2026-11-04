package com.Lyon_FindR.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "favorites",
        uniqueConstraints = @UniqueConstraint(name = "uk_favorites_user_place",
                columnNames = {"user_id", "place_id"}))
@Getter @Setter
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "added_at", nullable = false)
    private Instant addedAt;
}
