import { Injectable, inject } from '@angular/core';
import { AuthClient } from '../apis/auth/auth.client';
import { catchError, switchMap, tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { LoginRequest, LoginResponse, SignupRequest } from '../apis/auth/models';
import { SessionService } from './session.service';
import { Observable } from 'rxjs';
import { User } from '../../shared/models/user';

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private authClient: AuthClient, private session: SessionService) {}

  login(payload: LoginRequest): Observable<User> {
    return this.authClient.login(payload).pipe(
      tap((res) => {
        localStorage.setItem('token', res.token);
      }),
      switchMap(() => this.session.loadUser())
    );
  }

  signup(payload: SignupRequest): Observable<User> {
    return this.authClient.signup(payload).pipe(
      tap((res) => localStorage.setItem('token', res.token)),
      switchMap(() => this.session.loadUser())
    );
  }

  isAuthenticated(): boolean {
    return localStorage.getItem('token') != null;
  }

  logout(): void {
    this.session.clear();
    localStorage.removeItem('token');
  }
}
