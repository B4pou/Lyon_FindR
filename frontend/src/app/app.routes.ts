import { Routes } from '@angular/router';
import { PlacePage } from './components/place-page/place-page';

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
];
