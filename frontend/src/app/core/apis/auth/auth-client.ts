import { inject, Injectable } from '@angular/core';
import { API_CONFIG } from '../../config/apis.config';
import { HttpClient } from '@angular/common/http';
import { LoginRequest, LoginResponse, SignupRequest } from './auth.models';
import { Observable } from 'rxjs';
import { User } from '../../../shared/models/user.model';
import { SignatureData, SignatureResponse } from '../posts/post.model';

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
    return this.http.post<LoginResponse>(`${this.api_config.authBaseUrl}/signup`, payload);
  }

  getProfile(): Observable<User> {
    return this.http.get<User>(`${this.api_config.usersBaseUrl}/me`);
  }

  getSignature(): Observable<SignatureResponse> {

    return this.http.get<SignatureResponse>(`${this.api_config.authBaseUrl}/signature`);
  }

  uploadToCloudinary(file: File, sigData: SignatureData) {
    const formData = new FormData();

    formData.append('file', file);

    formData.append('api_key', sigData.apiKey);
    formData.append('timestamp', sigData.timestamp.toString());
    formData.append('signature', sigData.signature);

    formData.append('folder', sigData.folder);
    formData.append('upload_preset', sigData.upload_preset);
    formData.append('context', sigData.context);

    const url = `https://api.cloudinary.com/v1_1/${sigData.cloudName}/image/upload`;

    return this.http.post(url, formData);
  }
}
