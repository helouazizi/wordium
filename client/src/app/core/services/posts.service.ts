// core/services/posts.service.ts
import { Injectable, inject } from '@angular/core';
import { map, Observable, switchMap } from 'rxjs';
import { PostsClient } from '../apis/posts/posts.client';
import { PageResponse } from '../../shared/models/pagination.model';
import { CreatePostRequest, Post, Reaction, SignatureResponse } from '../apis/posts/modles';
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

  createPost(post: CreatePostRequest): Observable<Post> {
    return this.client.createPost(post);
  }
  getPostById(id: number): Observable<Post> {
    return this.client.getPostById(id);
  }

  deletePost(postId: number): Observable<void> {
    return this.client.deletePost(postId);
  }

  reactPost(postId: number, reaction: Reaction): Observable<void> {
    return this.client.reactPost(postId, reaction);
  }

  commentPost(postId: number, content: string): Observable<void> {
    return this.client.commentPost(postId, content);
  }

  reportPost(postId: number, reason: string): Observable<void> {
    return this.client.reportPost(postId, reason);
  }
}
