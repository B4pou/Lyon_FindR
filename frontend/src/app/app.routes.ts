import { Routes } from '@angular/router';
import { PlacePage } from './components/place-page/place-page';
import { PlaceDetail } from './components/place-detail/place-detail';

export const routes: Routes = [
  {
    path: '',
    component: PlacePage,
    pathMatch: 'full',
    data: { favoritesOnly: false },
  },
  {
    path: 'favorite',
    component: PlacePage,
    data: { favoritesOnly: true },
  },
  {
    path: 'places/:id',
    component: PlaceDetail,
  },
];
