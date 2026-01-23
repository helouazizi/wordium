import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FollowResponse, UpdateProfileRequest } from './users.model';
import { API_CONFIG } from '../../config/apis.config';
import { User } from '../../../shared/models/user.model';
import { PageRequest, PageResponse } from '../../../shared/models/pagination.model';

@Injectable({
  providedIn: 'root',
})
export class UsersClient {
  private http = inject(HttpClient);
  private config = inject(API_CONFIG);

  getMe(): Observable<User> {
    return this.http.get<User>(`${this.config.usersBaseUrl}/me`);
  }

  getUserById(userId: number): Observable<User> {
    return this.http.get<User>(`${this.config.usersBaseUrl}/${userId}`);
  }

  updateProfile(payload: UpdateProfileRequest): Observable<User> {
    const formData = new FormData();

    Object.entries(payload).forEach(([key, value]) => {
      if (value !== undefined && value !== null) {
        formData.append(key, value as any);
      }
    });

    return this.http.patch<User>(`${this.config.usersBaseUrl}/me`, formData);
  }

  follow(targetUserId: number): Observable<FollowResponse> {
    return this.http.post<FollowResponse>(
      `${this.config.usersBaseUrl}/${targetUserId}/follow`,
      null,
    );
  }

  unfollow(targetUserId: number): Observable<FollowResponse> {
    return this.http.delete<FollowResponse>(`${this.config.usersBaseUrl}/${targetUserId}/unfollow`);
  }

  getFollowers(userId: number): Observable<PageResponse<User>> {
    return this.http.get<PageResponse<User>>(`${this.config.usersBaseUrl}/${userId}/followers`);
  }
  
  // ---------- admin ----------
  getAllUsers(params?: PageRequest): Observable<any> {
    let httpParams = new HttpParams();

    if (params?.page !== undefined) {
      httpParams = httpParams.set('page', params.page);
    }
    if (params?.size !== undefined) {
      httpParams = httpParams.set('size', params.size);
    }
    if (params?.sort) {
      httpParams = httpParams.set('sort', params.sort);
    }

    return this.http.get(`${this.config.usersBaseUrl}/admin/accounts`, { params :httpParams });
  }

  banUser(id: number): Observable<void> {
    return this.http.patch<void>(`${this.config.usersBaseUrl}/admin/accounts/${id}/ban`, null);
  }

  unbanUser(id: number): Observable<void> {
    return this.http.patch<void>(`${this.config.usersBaseUrl}/admin/accounts/${id}/unban`, null);
  }

  changeRole(id: number, role: string): Observable<any> {
    return this.http.patch(`${this.config.usersBaseUrl}/admin/accounts/${id}/role`, { role });
  }
}
