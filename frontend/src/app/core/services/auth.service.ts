import { computed, inject, Injectable, signal } from '@angular/core';
import { User } from '../../shared/models/user.model';
import { Router } from '@angular/router';
import { AuthClient } from '../apis/auth-client';
import { LoginRequest, LoginResponse, SignupRequest } from '../apis/auth.models';
import { tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly client = inject(AuthClient);
  private readonly router = inject(Router);
  private readonly TOKEN_KEY = 'auth_token';

  private _user = signal<User | null>(null);
  private _isLoading = signal<boolean>(true);

  public user = this._user.asReadonly();
  public isLoading = this._isLoading.asReadonly();
  public isAuthenticated = computed(() => !!this._user());
  public isAdmin = computed(() => this._user()?.role === 'ADMIN');

  constructor() {
    this.hydrate();
  }

  private hydrate() {
    const token = localStorage.getItem(this.TOKEN_KEY);

    if (!token) {
      this._isLoading.set(false);
      return;
    }

    this.client.getProfile().subscribe({
      next: (user) => {
        this._user.set(user);
        this._isLoading.set(false);
      },
      error: () => {
        this.logout();
        this._isLoading.set(false);
      },
    });
  }

  currentUser = signal<User | null>({
    id: '1',
    username: 'John Doe',
    role: 'ADMIN',
    email: 'john@example.com',
    avatarUrl: 'https://api.dicebear.com/7.x/avataaars/svg?seed=John',
  });

  login(credentials: LoginRequest) {
    return this.client.login(credentials).pipe(tap((res) => this.setSession(res)));
  }

  register(data: SignupRequest) {
    return this.client.signup(data).pipe(tap((res) => this.setSession(res)));
  }

  logout() {
    this.clearSession();
  }

  private setSession(auth: LoginResponse) {
    localStorage.setItem('token', auth.token);
    this.router.navigate(['/feed']);
  }

  private clearSession() {
    localStorage.removeItem('token');
    this._user.set(null);
    this.router.navigate(['/login']);
  }
}
