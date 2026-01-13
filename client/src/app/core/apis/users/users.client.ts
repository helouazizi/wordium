import { HttpClient } from '@angular/common/http';
import { API_CONFIG } from '../api.config';
import { inject, Injectable } from '@angular/core';
import { User } from '../../../shared/models/user';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UsresClient {
  private http = inject(HttpClient);
  private config = inject(API_CONFIG);

  me(): Observable<User> {
    return this.http.get<User>(`${this.config.usersBaseUrl}/me`);
  }
}
