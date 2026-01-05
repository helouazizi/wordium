import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { UsresClient } from '../apis/users/users.client';
import { User } from '../../shared/models/user';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class SessionService {
  private userSubject = new BehaviorSubject<User | null>(null);
  user$ = this.userSubject.asObservable();

  constructor(private usersApi: UsresClient, private router: Router) {}

  loadUser(): Observable<User> {
    return this.usersApi.me().pipe(tap((user: User) => this.userSubject.next(user)));
  }

  hasAnyRole(roles: string[]): boolean {
    const user = this.userSubject.value;
    console.log('Session user :', user);
    return !!user && roles.includes(user.role);
  }
  get user(): User | null {
    return this.userSubject.value;
  }

  set user(user: User | null) {
    this.userSubject.next(user);
  }

  clear() {
    this.userSubject.next(null);
  }

  logout() {
    localStorage.removeItem('token');
    this.clear();
    this.router.navigate(['/login']);
  }
}
