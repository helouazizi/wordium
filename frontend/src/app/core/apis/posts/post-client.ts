// src/app/core/apis/posts/posts.client.ts
import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import {
  Comment,
  CreatePostRequest,
  Post,
  Reaction,
  SignatureData,
  SignatureResponse,
} from './post.model';
import { API_CONFIG } from '../../config/apis.config';
import { PageRequest, PageResponse } from '../../../shared/models/pagination.model';

@Injectable({ providedIn: 'root' })
export class PostsClient {
  private http = inject(HttpClient);
  private config = inject(API_CONFIG);

  feed(params?: PageRequest): Observable<PageResponse<Post>> {
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

    return this.http.get<PageResponse<Post>>(`${this.config.postsBaseUrl}`, {
      params: httpParams,
    });
  }

  getSignature(): Observable<SignatureResponse> {
    return this.http.get<SignatureResponse>(`${this.config.postsBaseUrl}/signature`);
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

    const url = `https://api.cloudinary.com/v1_1/${sigData.cloudName}/auto/upload`;

    return this.http.post(url, formData);
  }

  createPost(post: CreatePostRequest): Observable<Post> {
    return this.http.post<Post>(`${this.config.postsBaseUrl}`, post);
  }
  getPostById(id: number): Observable<Post> {
    return this.http.get<Post>(`${this.config.postsBaseUrl}/${id}`);
  }

  deletePost(postId: number): Observable<void> {
    return this.http.delete<void>(`${this.config.postsBaseUrl}/${postId}`);
  }
  reactPost(postId: number, reaction: Reaction): Observable<void> {
    return this.http.post<void>(`${this.config.postsBaseUrl}/${postId}/react`, { reaction });
  }
  commentPost(postId: number, content: string): Observable<void> {
    return this.http.post<void>(`${this.config.postsBaseUrl}/${postId}/comments`, { content });
  }
  reportPost(postId: number, reason: string): Observable<void> {
    return this.http.post<void>(`${this.config.postsBaseUrl}/${postId}/reports`, { reason });
  }

  getCommentsByPost(id: number, params?: PageRequest): Observable<PageResponse<Comment>> {
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
    return this.http.get<PageResponse<Comment>>(`${this.config.postsBaseUrl}/${id}/comments`, {
      params: httpParams,
    });
  }
}
