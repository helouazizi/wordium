import { Injectable, signal, computed, inject } from '@angular/core';
import { Router } from '@angular/router';
import { UsresClient } from '../apis/users/users.client';
import { User } from '../../shared/models/user';
import { tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class SessionService {
  private readonly usersApi = inject(UsresClient);
  private readonly router = inject(Router);

  private readonly _user = signal<User | null>(null);

  readonly user = this._user.asReadonly();

  readonly isLoggedIn = computed(() => this._user() !== null);

  readonly role = computed(() => this._user()?.role ?? null);

  loadUser() {
    return this.usersApi.me().pipe(tap((user) => this._user.set(user)));
  }

  getUser(): User | null {
    return this._user();
  }

  setUser(user: User | null) {
    this._user.set(user);
  }

  clear() {
    this._user.set(null);
  }

  hasAnyRole(roles: string[]): boolean {
    const user = this._user();
    return !!user && roles.includes(user.role);
  }

  logout() {
    localStorage.removeItem('token');
    this.clear();
    this.router.navigate(['/login']);
  }
}
