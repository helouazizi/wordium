import { Routes } from '@angular/router';
import { LayoutComponent } from './layout/layout.component';
import { authGuard } from './core/guards/auth-guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./pages/auth/login/login').then((m) => m.Login),
  },
  {
    path: 'register',
    loadComponent: () => import('./pages/auth/register/register').then((m) => m.Register),
  },

  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      {
        path: 'feed',
        loadComponent: () => import('./pages/feed/feed').then((m) => m.Feed),
      },
      {
        path: 'write', //  whrite blog
        loadComponent: () => import('./pages/post-editor/post-editor').then((c) => c.PostEditor),
      },
      {
        path: 'posts/:id',
        loadComponent: () => import('./pages/post-detail/post-detail').then((c) => c.PostDetail),
      },
      {
        path: 'profiles/:id', //  whrite blog
        loadComponent: () => import('./pages/profile/profile').then((c) => c.Profile),
      },
      {
        path: 'dashboard', //  whrite blog
        loadComponent: () => import('./pages/dashboard/dashboard').then((c) => c.Dashboard),
      },
      {
        path: '',
        redirectTo: 'feed',
        pathMatch: 'full',
      },
    ],
  },

  {
    path: '**',
    redirectTo: 'feed',
  },
];
