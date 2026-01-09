// src/app/core/apis/posts/posts.client.ts
import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../api.config';
import { PageResponse } from '../../../shared/models/pagination.model';
import { Post } from './modles';
import { PageRequest } from '../../../shared/models/page-request.model';

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
}
