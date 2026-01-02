import { inject, Injectable, signal } from '@angular/core';
import { AuthControllerService } from '../api/auth/api/authController.service';
import { LoginRequest } from '../api/auth/model/loginRequest';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class AuthService {
  //   private _token = signal<string | null>(null);
  private router = inject(Router);

  constructor(private authApi: AuthControllerService) {}

  login(payload: LoginRequest) {
    return this.authApi.login(payload).pipe(
      tap((res) => {
        // this._token.set(res.token);
        localStorage.setItem('token', 'res.token');
      })
    );
  }

  logout() {
    // this._token.set(null);
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  //   get token() {
  //     return this._token();
  //   }

  //   isLoggedIn() {
  //     return !!this._token();
  //   }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('token');
  }
}
