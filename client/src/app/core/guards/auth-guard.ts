import { ActivatedRouteSnapshot, CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { inject, Injectable } from '@angular/core';
import { SessionService } from '../services/session.service';

export const authGuard: CanActivateFn = (route, state) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (!auth.isAuthenticated()) {
    router.navigate(['/login'], {
      queryParams: { redirect: state.url },
    });
    return false;
  }

  return true;
};

export const roleGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const session = inject(SessionService);
  const allowedRoles = route.data['roles'] as string[];

  return session.hasAnyRole(allowedRoles);
};
