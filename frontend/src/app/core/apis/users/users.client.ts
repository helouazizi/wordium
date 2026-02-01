import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CountResponse, FollowResponse, UpdateProfileRequest } from './users.model';
import { API_CONFIG } from '../../config/apis.config';
import { User } from '../../../shared/models/user.model';
import { PageRequest, PageResponse } from '../../../shared/models/pagination.model';
import { Report, SignatureData, SignatureResponse } from '../posts/post.model';

@Injectable({
  providedIn: 'root',
})
export class UsersClient {
  private http = inject(HttpClient);
  private config = inject(API_CONFIG);

  getMe(): Observable<User> {
    return this.http.get<User>(`${this.config.usersBaseUrl}/me`);
  }

  getUsersReportsCount(): Observable<CountResponse> {
    return this.http.get<CountResponse>(`${this.config.usersBaseUrl}/admin/reports/users/count`);
  }
  getPostReporstCount(): Observable<CountResponse> {
    return this.http.get<CountResponse>(`${this.config.usersBaseUrl}/admin/reports/posts/count`);
  }

  getUsersCount(): Observable<CountResponse> {
    return this.http.get<CountResponse>(`${this.config.usersBaseUrl}/admin/accounts/count`);
  }

  getPostCount(): Observable<CountResponse> {
    return this.http.get<CountResponse>(`${this.config.usersBaseUrl}/admin/posts/count`);
  }

  getUserById(targetId: number): Observable<User> {
    return this.http.get<User>(`${this.config.usersBaseUrl}/${targetId}`);
  }

  updateProfile(payload: UpdateProfileRequest): Observable<User> {
    return this.http.patch<User>(`${this.config.usersBaseUrl}/me`, payload);
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

  getFollowers(userId: number, params: PageRequest): Observable<PageResponse<User>> {
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
    return this.http.get<PageResponse<User>>(`${this.config.usersBaseUrl}/${userId}/followers`, {
      params: httpParams,
    });
  }

  getFollowing(userId: number, params: PageRequest): Observable<PageResponse<User>> {
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
    return this.http.get<PageResponse<User>>(`${this.config.usersBaseUrl}/${userId}/following`, {
      params: httpParams,
    });
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

    return this.http.get(`${this.config.usersBaseUrl}/admin/accounts`, { params: httpParams });
  }

  getPostReports(params?: PageRequest): Observable<PageResponse<Report>> {
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

    return this.http.get<PageResponse<Report>>(`${this.config.usersBaseUrl}/admin/reports/posts`, {
      params: httpParams,
    });
  }

  getUserReports(params?: PageRequest): Observable<PageResponse<Report>> {
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

    return this.http.get<PageResponse<Report>>(`${this.config.usersBaseUrl}/admin/reports/users`, {
      params: httpParams,
    });
  }

  resolvePostReport(id: number): Observable<void> {
    return this.http.patch<void>(
      `${this.config.usersBaseUrl}/admin/reports/posts/${id}/resolve`,
      null,
    );
  }
  resolveUserReport(id: number): Observable<void> {
    return this.http.patch<void>(
      `${this.config.usersBaseUrl}/admin/reports/users/${id}/resolve`,
      null,
    );
  }

  deletePostReport(id: number): Observable<void> {
    return this.http.delete<void>(`${this.config.usersBaseUrl}/admin/reports/posts/${id}`);
  }

  deleteUserReport(id: number): Observable<void> {
    return this.http.delete<void>(`${this.config.usersBaseUrl}/admin/reports/users/${id}`);
  }

  banUser(id: number): Observable<void> {
    return this.http.patch<void>(`${this.config.usersBaseUrl}/admin/accounts/${id}/ban`, null);
  }

  unbanUser(id: number): Observable<void> {
    return this.http.patch<void>(`${this.config.usersBaseUrl}/admin/accounts/${id}/unban`, null);
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.config.usersBaseUrl}/admin/accounts/${id}`);
  }

  changeRole(id: number, role: string): Observable<any> {
    return this.http.patch(`${this.config.usersBaseUrl}/admin/accounts/${id}/role`, { role });
  }

  getSignature(): Observable<SignatureResponse> {
    return this.http.get<SignatureResponse>(`${this.config.usersBaseUrl}/signature`);
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
