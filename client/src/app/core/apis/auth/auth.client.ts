// src/app/core/apis/auth/auth.client.ts
import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginRequest, LoginResponse } from './models';
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

  //   refreshToken(): Observable<LoginResponse> {
  //     return this.http.post<LoginResponse>(`${this.baseUrl}/refresh`, {});
  //   }
}
