import { HttpClient } from '@angular/common/http';
import { API_CONFIG } from '../api.config';
import { inject, Injectable } from '@angular/core';
import { User } from '../../../models/user';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UsresClient {
  private http = inject(HttpClient);
  private config = inject(API_CONFIG);

  private _me$?: Observable<User>; // cache the request

  me(): Observable<User> {
    if (!this._me$) {
      this._me$ = this.http.get<User>(`${this.config.usersBaseUrl}/me`);
    }
    return this._me$;
  }
}
