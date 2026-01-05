// src/app/core/apis/auth/auth.client.ts
import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginRequest, LoginResponse, SignupRequest } from './models';
import { API_CONFIG } from '../api.config';

@Injectable({ providedIn: 'root' })
export class AuthClient {
  private http = inject(HttpClient);
  private config = inject(API_CONFIG);

  login(payload: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.config.authBaseUrl}/login`, payload, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    });
  }

  signup(data: SignupRequest): Observable<LoginResponse> {
    const formData = new FormData();

    formData.append('email', data.email);
    formData.append('username', data.username);
    formData.append('password', data.password);

    if (data.bio) formData.append('bio', data.bio);
    if (data.location) formData.append('location', data.location);
    if (data.avatar) formData.append('avatar', data.avatar);

    return this.http.post<LoginResponse>(`${this.config.authBaseUrl}/signup`, formData);
  }

  
}
