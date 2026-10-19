import { Component, inject, OnInit, signal, computed } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

import { PlaceCard } from '../place-card/place-card';
import { PlaceService, Place } from '../../services/place.service';

@Component({
  selector: 'app-place-page',
  standalone: true,
  imports: [CommonModule, PlaceCard, FormsModule],
  templateUrl: './place-page.html',
  styleUrl: './place-page.css',
})
export class PlacePage implements OnInit {
  places = signal<Place[]>([]);
  loading = signal(true);
  errorMessage = signal('');

  favoritesOnly = signal(false);

  showCreateForm = signal(false);

  visiblePlaces = computed(() => {
    const allPlaces = this.places();

    return this.favoritesOnly() ? allPlaces.filter((place) => place.isFavorite) : allPlaces;
  });

  private readonly placeService = inject(PlaceService);
  private readonly route = inject(ActivatedRoute);

  newPlace = {
    name: '',
    category: '' as Place['category'],
    note: 0,
    isFavorite: false,
  };

  ngOnInit(): void {
    this.route.data.subscribe((data) => {
      this.favoritesOnly.set(data['favoritesOnly'] === true);
    });

    this.loadPlaces();
  }

  loadPlaces(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.placeService.getPlaces().subscribe({
      next: (places) => {
        this.places.set(places);
        this.loading.set(false);
      },
      error: (error) => {
        console.error('Erreur lors du chargement des lieux', error);
        this.errorMessage.set('Impossible de charger les lieux.');
        this.loading.set(false);
      },
    });
  }

  onFavoriteChanged(updatedPlace: Place): void {
    this.placeService.updatePlace(updatedPlace).subscribe({
      next: (savedPlace) => {
        this.places.update((places) =>
          places.map((place) => (place.id === savedPlace.id ? savedPlace : place)),
        );
      },
      error: (error) => {
        console.error('Erreur lors de la mise à jour du favori', error);
        this.errorMessage.set('Impossible de sauvegarder le favori.');
      },
    });
  }

  createPlace(): void {
    const placeToCreate = {
      ...this.newPlace,
      name: this.newPlace.name.trim(),
    };

    if (
      !placeToCreate.name ||
      !placeToCreate.category ||
      placeToCreate.note < 1 ||
      placeToCreate.note > 5
    ) {
      return;
    }

    this.placeService.createPlace(placeToCreate).subscribe({
      next: (createdPlace) => {
        this.places.update((places) =>
          [...places, createdPlace].sort((a, b) => a.name.localeCompare(b.name, 'fr')),
        );

        this.newPlace = {
          name: '',
          category: '' as Place['category'],
          note: 1,
          isFavorite: false,
        };

        this.showCreateForm.set(false);
      },
      error: (error) => {
        console.error('Erreur lors de la création du lieu', error);
        this.errorMessage.set('Impossible de créer le lieu.');
      },
    });
  }

  onOverlayClick(event: MouseEvent): void {
    if (event.target === event.currentTarget) {
      this.showCreateForm.set(false);
    }
  }
}
