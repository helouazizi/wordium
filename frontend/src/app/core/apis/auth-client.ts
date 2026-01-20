import { inject, Injectable } from '@angular/core';
import { API_CONFIG } from '../config/apis.config';
import { HttpClient } from '@angular/common/http';
import { LoginRequest, LoginResponse, SignupRequest } from './auth.models';
import { Observable } from 'rxjs';
import { User } from '../../shared/models/user.model';

@Injectable({
  providedIn: 'root',
})
export class AuthClient {
  private readonly http = inject(HttpClient);
  private api_config = inject(API_CONFIG);

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.api_config.authBaseUrl}/login`, credentials);
  }

  signup(payload: SignupRequest): Observable<LoginResponse> {
    const body = new FormData();

    body.append('email', payload.email);
    body.append('username', payload.username);
    body.append('password', payload.password);

    if (payload.bio) body.append('bio', payload.bio);
    if (payload.location) body.append('location', payload.location);
    if (payload.avatar) body.append('avatar', payload.avatar);

    return this.http.post<LoginResponse>(`${this.api_config.authBaseUrl}/signup`, body);
  }

  getProfile(): Observable<User> {
    return this.http.get<User>(`${this.api_config.usersBaseUrl}/me`);
  }
}
