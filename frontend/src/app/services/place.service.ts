import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Place {
  id: number;
  name: string;
  category: string;
  note: number;
  isFavorite: boolean;
}

@Injectable({
  providedIn: 'root',
})
export class PlaceService {
  private apiUrl = 'http://localhost:8080';

  private http = inject(HttpClient);

  getPlaces(): Observable<Place[]> {
    return this.http.get<Place[]>(`${this.apiUrl}/places`);
  }

  getPlace(id: number): Observable<Place> {
    return this.http.get<Place>(`${this.apiUrl}/place/${id}`);
  }

  updatePlace(place: Place): Observable<Place> {
    return this.http.put<Place>(`${this.apiUrl}/place/${place.id}`, place);
  }

  createPlace(place: Omit<Place, 'id'>) {
    return this.http.post<Place>(`${this.apiUrl}/place`, place);
  }

  deletePlace(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/place/${id}`);
  }
}
