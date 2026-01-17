import { Routes } from '@angular/router';
import { authGuard, roleGuard } from './core/guards/auth-guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./features/auth/pages/login/login').then((m) => m.Login),
  },
  {
    path: 'signup',
    loadComponent: () => import('./features/auth/pages/signup/signup').then((m) => m.Signup),
  },
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () => import('./layout/app-layout/app-layout').then((c) => c.AppLayout),
    children: [
      {
        path: '', // feed
        loadComponent: () => import('./features/posts/feed/feed').then((c) => c.Feed),
      },
      {
        path: 'posts/:id',
        loadComponent: () =>
          import('./features/posts/post-details/post-details').then((c) => c.PostDetails),
      },
      {
        path: 'dashboard',
        canActivate: [roleGuard],
        data: { roles: ['ADMIN'] },
        loadChildren: () =>
          import('./features/dashboard/dashboard.module').then((m) => m.DashboardModule),
      },
    ],
  },

  {
    path: '**',
    redirectTo: '/',
  },
];
