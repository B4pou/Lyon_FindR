import { Component, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Place } from '../../services/place.service';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-place-card',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './place-card.html',
  styleUrl: './place-card.css',
})
export class PlaceCard {
  place = input.required<Place>();

  favoriteChanged = output<Place>();
  toggleFavorite(): void {
    const updatedPlace: Place = {
      ...this.place(),
      isFavorite: !this.place().isFavorite,
    };

    this.favoriteChanged.emit(updatedPlace);
  }
}
