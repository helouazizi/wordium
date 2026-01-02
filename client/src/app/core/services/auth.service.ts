// src/app/core/services/auth.service.ts
import { Injectable, inject } from '@angular/core';
import { AuthClient } from '../apis/auth/auth.client';
import { catchError, tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { LoginRequest, LoginResponse } from '../apis/auth/models';
import { throwError } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private client = inject(AuthClient);
  private router = inject(Router);

  login(payload: LoginRequest) {
    return this.client.login(payload).pipe(
      tap((res: LoginResponse) => {
        localStorage.setItem('token', res.token);
      })
    );
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}
