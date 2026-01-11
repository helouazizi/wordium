// core/services/posts.service.ts
import { Injectable, inject } from '@angular/core';
import { map, Observable, switchMap } from 'rxjs';
import { PostsClient } from '../apis/posts/posts.client';
import { PageResponse } from '../../shared/models/pagination.model';
import { Post, SignatureResponse } from '../apis/posts/modles';
import { PageRequest } from '../../shared/models/page-request.model';

@Injectable({ providedIn: 'root' })
export class PostsService {
  private client = inject(PostsClient);

  getSignature(): Observable<SignatureResponse> {
    return this.client.getSignature();
  }

  uploadToCloudinary(file: File, sigData: any) {
    return this.client.uploadToCloudinary(file, sigData);
  }

  uploadImage(file: File) {
    return this.getSignature().pipe(
      switchMap((res) => this.uploadToCloudinary(file, res.data)),
      map((r: any) => r)
    );
  }

  getFeed(params: PageRequest): Observable<PageResponse<Post>> {
    return this.client.feed(params);
  }
}
