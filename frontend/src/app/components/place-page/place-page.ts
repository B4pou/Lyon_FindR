import {
  Component,
  inject,
  OnInit,
  signal,
  computed
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

import { PlaceCard } from '../place-card/place-card';
import { PlaceService, Place } from '../../services/place.service';

@Component({
  selector: 'app-place-page',
  standalone: true,
  imports: [CommonModule, PlaceCard],
  templateUrl: './place-page.html',
  styleUrl: './place-page.css'
})
export class PlacePage implements OnInit {
  places = signal<Place[]>([]);
  loading = signal(true);
  errorMessage = signal('');

  favoritesOnly = signal(false);

  visiblePlaces = computed(() => {
    const allPlaces = this.places();

    return this.favoritesOnly()
      ? allPlaces.filter(place => place.isFavorite)
      : allPlaces;
  });

  private readonly placeService = inject(PlaceService);
  private readonly route = inject(ActivatedRoute);

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.favoritesOnly.set(data['favoritesOnly'] === true);
    });

    this.loadPlaces();
  }

  loadPlaces(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.placeService.getPlaces().subscribe({
      next: places => {
        this.places.set(places);
        this.loading.set(false);
      },
      error: error => {
        console.error('Erreur lors du chargement des lieux', error);
        this.errorMessage.set('Impossible de charger les lieux.');
        this.loading.set(false);
      }
    });
  }

  onFavoriteChanged(updatedPlace: Place): void {
    this.placeService.updatePlace(updatedPlace).subscribe({
      next: savedPlace => {
        this.places.update(places =>
          places.map(place =>
            place.id === savedPlace.id ? savedPlace : place
          )
        );
      },
      error: error => {
        console.error(
          'Erreur lors de la mise à jour du favori',
          error
        );
        this.errorMessage.set(
          'Impossible de sauvegarder le favori.'
        );
      }
    });
  }
}
