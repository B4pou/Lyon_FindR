package com.Lyon_FindR.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@Data
@Entity
@Table(name = "place")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "La catégorie est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @NotNull(message = "La note est obligatoire")
    @Min(value = 0, message = "La note doit être au minimum de 0")
    @Max(value = 5, message = "La note doit être au maximum de 5")
    @Column(nullable = false)
    private Integer note;

    @Column(nullable = false)
    private Boolean isFavorite = false;

    private String address;
}
