// src/app/core/services/auth.service.ts
import { Injectable, inject } from '@angular/core';
import { AuthClient } from '../apis/auth/auth.client';
import { catchError, tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { LoginRequest, LoginResponse, SignupRequest } from '../apis/auth/models';
import { SessionService } from './session.service';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private client = inject(AuthClient);
  private router = inject(Router);
  private session = inject(SessionService);
  login(payload: LoginRequest) {
    return this.client.login(payload).pipe(
      tap((res: LoginResponse) => {
        this.session.clear();
        localStorage.setItem('token', res.token);
        this.session.loadUser().subscribe();
      })
    );
  }

  signup(payload: SignupRequest) {
    return this.client.signup(payload).pipe(
      tap((res: LoginResponse) => {
        this.session.clear();
        localStorage.setItem('token', res.token);
        this.session.loadUser().subscribe();
      })
    );
  }



  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}
