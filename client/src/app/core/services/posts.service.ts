// core/services/posts.service.ts
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { PostsClient } from '../apis/posts/posts.client';
import { PageResponse } from '../../shared/models/pagination.model';
import { Post } from '../apis/posts/modles';
import { PageRequest } from '../../shared/models/page-request.model';

@Injectable({ providedIn: 'root' })
export class PostsService {
  private client = inject(PostsClient);

  getFeed(params: PageRequest): Observable<PageResponse<Post>> {
    return this.client.feed(params);
  }
}
