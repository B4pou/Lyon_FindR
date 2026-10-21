import { Component, inject, OnInit, signal } from '@angular/core';
import {ActivatedRoute, RouterLink} from '@angular/router';
import { Place, PlaceService } from '../../services/place.service';

@Component({
  imports: [RouterLink],
  selector: 'app-place-detail',
  styleUrl: './place-detail.css',
  templateUrl: './place-detail.html',
})
export class PlaceDetail implements OnInit {
  private route = inject(ActivatedRoute);
  private placeService = inject(PlaceService);

  place = signal<Place | null>(null);
  loading = signal(true);
  errorMessage = signal('');

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    if (!Number.isInteger(id) || id <= 0) {
      this.errorMessage.set('Identifiant de lieu invalide.');
      this.loading.set(false);
      return;
    }

    this.placeService.getPlace(id).subscribe({
      next: (place) => {
        this.place.set(place);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Impossible de récupérer ce lieu.');
        this.loading.set(false);
      },
    });
  }
}
