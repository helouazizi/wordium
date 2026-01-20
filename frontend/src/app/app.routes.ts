import { Routes } from '@angular/router';
import { LayoutComponent } from './layout/layout.component';
import { Feed } from './pages/feed/feed';

export const routes: Routes = [
  {
    path: '',
    component: LayoutComponent, // The Shell
    children: [
      { path: '', component: Feed }, // The Content
    ]
  }
];