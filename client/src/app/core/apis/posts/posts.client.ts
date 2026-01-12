// src/app/core/apis/posts/posts.client.ts
import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../api.config';
import { PageResponse } from '../../../shared/models/pagination.model';
import { PageRequest } from '../../../shared/models/page-request.model';
import { CreatePostRequest, Post, SignatureData, SignatureResponse } from './modles';
import { appConfig } from '../../../app.config';

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

    // File
    formData.append('file', file);

    // Required for signed upload
    formData.append('api_key', sigData.apiKey);
    formData.append('timestamp', sigData.timestamp.toString());
    formData.append('signature', sigData.signature);

    // Must match backend signature
    formData.append('folder', sigData.folder);
    formData.append('upload_preset', sigData.upload_preset);

    const url = `https://api.cloudinary.com/v1_1/${sigData.cloudName}/auto/upload`;

    return this.http.post(url, formData);
  }

  createPost(post: CreatePostRequest): Observable<Post> {
    return this.http.post<Post>(`${this.config.postsBaseUrl}`, post);
  }
}
