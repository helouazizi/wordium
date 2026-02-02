import { inject, Injectable } from '@angular/core';
import { Observable, switchMap, tap } from 'rxjs';
import { UsersClient } from '../apis/users/users.client';
import { User } from '../../shared/models/user.model';
import { CountResponse, FollowResponse, UpdateProfileRequest } from '../apis/users/users.model';
import { PageRequest, PageResponse } from '../../shared/models/pagination.model';
import { Report, SignatureResponse } from '../apis/posts/post.model';

@Injectable({
  providedIn: 'root',
})
export class UsersService {
  private client = inject(UsersClient);

  getMyProfile(): Observable<User> {
    return this.client.getMe();
  }

  getUsersReportsCount(): Observable<CountResponse> {
    return this.client.getUsersReportsCount();
  }

  getPostReporstCount(): Observable<CountResponse> {
    return this.client.getPostReporstCount();
  }

  getUsersCount(): Observable<CountResponse> {
    return this.client.getUsersCount();
  }
  getPostCount(): Observable<CountResponse> {
    return this.client.getPostCount();
  }

  getUserProfile(userId: number): Observable<User> {
    return this.client.getUserById(userId);
  }

  updateMyProfile(payload: UpdateProfileRequest): Observable<User> {
    return this.client.updateProfile(payload);
  }

  followUser(targetUserId: number): Observable<FollowResponse> {
    return this.client.follow(targetUserId);
  }

  unfollowUser(targetUserId: number): Observable<FollowResponse> {
    return this.client.unfollow(targetUserId);
  }

  getUserFollowers(userId: number, params: PageRequest): Observable<PageResponse<User>> {
    return this.client.getFollowers(userId, params);
  }
  getSearchUsers(params: PageRequest): Observable<PageResponse<User>> {
    return this.client.getSearchUsers( params);
  }
  getUserFollowing(userId: number, params: PageRequest): Observable<PageResponse<User>> {
    return this.client.getFollowing(userId, params);
  }
  getAllUsers(params?: PageRequest): Observable<PageResponse<User>> {
    return this.client.getAllUsers(params);
  }

  getPostReports(params?: PageRequest): Observable<PageResponse<Report>> {
    return this.client.getPostReports(params);
  }

  getUserReports(params?: PageRequest): Observable<PageResponse<Report>> {
    return this.client.getUserReports(params);
  }

  resolveUserReport(id: number): Observable<void> {
    return this.client.resolveUserReport(id);
  }

  resolvePostReport(id: number): Observable<void> {
    return this.client.resolvePostReport(id);
  }

  deletePostReport(id: number): Observable<void> {
    return this.client.deletePostReport(id);
  }

  deleteUserReport(id: number): Observable<void> {
    return this.client.deleteUserReport(id);
  }

  banUser(userId: number): Observable<void> {
    return this.client.banUser(userId);
  }

  unbanUser(userId: number): Observable<void> {
    return this.client.unbanUser(userId);
  }

  deleteUser(userId: number): Observable<void> {
    return this.client.deleteUser(userId);
  }

  changeUserRole(userId: number, role: string): Observable<void> {
    return this.client.changeRole(userId, role);
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
