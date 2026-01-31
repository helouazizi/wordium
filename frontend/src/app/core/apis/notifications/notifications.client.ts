// src/app/core/apis/posts/posts.client.ts
import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_CONFIG } from '../../config/apis.config';
import { PageRequest, PageResponse } from '../../../shared/models/pagination.model';
import { NotificationInter } from './notification.model';

@Injectable({ providedIn: 'root' })
export class NotificationsClient {
  private http = inject(HttpClient);
  private config = inject(API_CONFIG);

  getNotifications(params?: PageRequest): Observable<PageResponse<NotificationInter>> {
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

    return this.http.get<PageResponse<NotificationInter>>(`${this.config.wsBaseUrl}`, {
      params: httpParams,
    });
  }
}
