import { computed, inject, Injectable, signal } from '@angular/core';
import { User } from '../../shared/models/user.model';
import { Router } from '@angular/router';
import { AuthClient } from '../apis/auth/auth-client';
import { LoginRequest, LoginResponse, SignupRequest } from '../apis/auth/auth.models';
import { Observable, switchMap, tap } from 'rxjs';
import { SignatureResponse } from '../apis/posts/post.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly client = inject(AuthClient);
  private readonly router = inject(Router);
  private readonly TOKEN_KEY = 'token';

  private _user = signal<User | null>(null);
  private _isLoading = signal<boolean>(true);

  public user = this._user.asReadonly();
  public isLoading = this._isLoading.asReadonly();
  public isAuthenticated = computed(() => !!this._user());
  public isAdmin = computed(() => this._user()?.role === 'ADMIN');

  initializeAuth(): Promise<void> {
    return new Promise((resolve) => {
      const token = localStorage.getItem('token');
      if (!token) {
        this._isLoading.set(false);
        resolve();
        return;
      }

      this.client.getProfile().subscribe({
        next: (user) => {
          this._user.set(user);
          this._isLoading.set(false);
          resolve();
        },
        error: () => {
          this.logout();
          this._isLoading.set(false);
          resolve();
        },
      });
    });
  }

  login(credentials: LoginRequest) {
    return this.client.login(credentials).pipe(
      switchMap((res: LoginResponse) => {
        localStorage.setItem(this.TOKEN_KEY, res.token);
        return this.client.getProfile();
      }),
      tap((user: User) => {
        this._user.set(user);
        this._isLoading.set(false);
        this.router.navigate(['/feed']);
      }),
    );
  }

  register(data: SignupRequest) {
    return this.client.signup(data).pipe(
      switchMap((res: LoginResponse) => {
        localStorage.setItem(this.TOKEN_KEY, res.token);
        return this.client.getProfile();
      }),
      tap((user: User) => {
        this._user.set(user);
        this._isLoading.set(false);
        this.router.navigate(['/feed']);
      }),
    );
  }

  logout() {
    this.clearSession();
  }

 

  private clearSession() {
    localStorage.removeItem(this.TOKEN_KEY);
    this._user.set(null);
    this.router.navigate(['/login']);
  }

    getSignature(): Observable<SignatureResponse> {
      return this.client.getSignature();
    }
  
    uploadToCloudinary(file: File, sigData: any) {
      return this.client.uploadToCloudinary(file, sigData);
    }
  
    uploadImage(file: File): Observable<any> {
      return this.getSignature().pipe(switchMap((res) => this.uploadToCloudinary(file, res.data)));
    }
}
