import { Component, inject, OnInit, signal, computed } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

import { PlaceCard } from '../place-card/place-card';
import { PlaceService, Place } from '../../services/place.service';

type PlaceCategory = 'RESTAURANT' | 'LOISIR' | 'VISITE' | 'PLACE' | 'PARC';

type SortField = 'name' | 'note' | 'category';
type SortDirection = 'ASC' | 'DESC';

@Component({
  selector: 'app-place-page',
  standalone: true,
  imports: [CommonModule, PlaceCard, FormsModule],
  templateUrl: './place-page.html',
  styleUrl: './place-page.css',
})
export class PlacePage implements OnInit {
  private readonly placeService = inject(PlaceService);
  private readonly route = inject(ActivatedRoute);

  places = signal<Place[]>([]);
  loading = signal(true);
  errorMessage = signal('');

  favoritesOnly = signal(false);
  showCreateForm = signal(false);

  categories: { value: PlaceCategory; label: string }[] = [
    { value: 'RESTAURANT', label: 'Restaurants' },
    { value: 'LOISIR', label: 'Loisirs' },
    { value: 'VISITE', label: 'Visites' },
    { value: 'PLACE', label: 'Places' },
    { value: 'PARC', label: 'Parcs' },
  ];

  selectedCategories = signal<PlaceCategory[]>([]);
  sortBy = signal<SortField>('name');
  direction = signal<SortDirection>('ASC');

  visiblePlaces = computed(() => {
    const allPlaces = this.places();

    return this.favoritesOnly() ? allPlaces.filter((place) => place.isFavorite) : allPlaces;
  });

  newPlace = {
    name: '',
    category: '' as Place['category'],
    note: 1,
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

    this.placeService
      .getPlaces(this.selectedCategories(), this.sortBy(), this.direction())
      .subscribe({
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

  onPlaceDeleted(id: number): void {
    this.places.update((places) => places.filter((place) => place.id !== id));
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
      next: () => {
        this.newPlace = {
          name: '',
          category: '' as Place['category'],
          note: 1,
          isFavorite: false,
        };

        this.showCreateForm.set(false);
        this.loadPlaces();
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

  toggleCategory(category: PlaceCategory): void {
    const selected = this.selectedCategories();

    this.selectedCategories.set(
      selected.includes(category)
        ? selected.filter((item) => item !== category)
        : [...selected, category],
    );

    this.loadPlaces();
  }

  changeSortBy(value: string): void {
    if (value !== 'name' && value !== 'note' && value !== 'category') {
      return;
    }

    this.sortBy.set(value);
    this.loadPlaces();
  }

  toggleDirection(): void {
    this.direction.update((current) => (current === 'ASC' ? 'DESC' : 'ASC'));

    this.loadPlaces();
  }
}
