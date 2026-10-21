import {Component, inject, input, output} from '@angular/core';
import { CommonModule } from '@angular/common';
import {Place, PlaceService} from '../../services/place.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-place-card',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './place-card.html',
  styleUrl: './place-card.css',
})
export class PlaceCard {
  private placeService = inject(PlaceService);
  place = input.required<Place>();
  placeDeleted = output<number>();

  favoriteChanged = output<Place>();
  toggleFavorite(): void {
    const updatedPlace: Place = {
      ...this.place(),
      isFavorite: !this.place().isFavorite,
    };

    this.favoriteChanged.emit(updatedPlace);
  }

  deletePlace(event: MouseEvent): void {
    event.stopPropagation();

    const currentPlace = this.place();

    if (!confirm(`Voulez-vous vraiment supprimer "${currentPlace.name}" ?`)) {
      return;
    }

    this.placeService.deletePlace(currentPlace.id).subscribe({
      next: () => {
        this.placeDeleted.emit(currentPlace.id);
      },
      error: (error) => {
        console.error('Erreur lors de la suppression du lieu :', error);
        alert('Impossible de supprimer ce lieu.');
      },
    });
  }

  updateNote(note: number, event: MouseEvent): void {
    event.stopPropagation();

    if (note < 0 || note > 5) {
      return;
    }

    const updatedPlace: Place = {
      ...this.place(),
      note,
    };

    this.favoriteChanged.emit(updatedPlace);
  }
}
