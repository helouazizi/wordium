import { Routes } from '@angular/router';
import { LayoutComponent } from './layout/layout.component';
import { authGuard } from './core/guards/auth-guard';


export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./pages/auth/login/login').then(m => m.Login)
  },
  {
    path: 'register',
    loadComponent: () => import('./pages/auth/register/register').then(m => m.Register)
  },


  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard], 
    children: [
      { 
        path: 'feed', 
        loadComponent: () => import('./pages/feed/feed').then(m => m.Feed) 
      },
      { 
        path: '', 
        redirectTo: 'feed', 
        pathMatch: 'full' 
      }
    ]
  },

  // FALLBACK
  { 
    path: '**', 
    redirectTo: 'feed' 
  }
];