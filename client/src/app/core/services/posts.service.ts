// core/services/posts.service.ts
import { Injectable, inject, signal } from '@angular/core';
import { catchError, map, Observable, of, switchMap, tap } from 'rxjs';
import { PostsClient } from '../apis/posts/posts.client';
import { PageResponse } from '../../shared/models/pagination.model';
import { CreatePostRequest, Post, Reaction, SignatureResponse } from '../apis/posts/modles';
import { PageRequest } from '../../shared/models/page-request.model';

@Injectable({ providedIn: 'root' })
export class PostsService {
  private client = inject(PostsClient);

  private readonly _posts = signal<Post[]>([]);
  readonly posts = this._posts.asReadonly();

  getSignature(): Observable<SignatureResponse> {
    return this.client.getSignature();
  }

  uploadToCloudinary(file: File, sigData: any) {
    return this.client.uploadToCloudinary(file, sigData);
  }

  uploadImage(file: File): Observable<any> {
    return this.getSignature().pipe(switchMap((res) => this.uploadToCloudinary(file, res.data)));
  }

  getFeed(params: PageRequest): Observable<PageResponse<Post>> {
    return this.client.feed(params).pipe(
      tap((res) => {
        if (params.page === 1) {
          this._posts.set(res.data);
        } else {
          // Append new posts, but filter duplicates if exist
          this._posts.update((prev) => {
            const newIds = new Set(res.data.map((p) => p.id));
            return [...prev.filter((p) => !newIds.has(p.id)), ...res.data];
          });
        }
      }),
    );
  }

  createPost(post: CreatePostRequest): Observable<Post> {
    return this.client
      .createPost(post)
      .pipe(tap((newPost) => this._posts.update((prev) => [newPost, ...prev])));
  }

  getPostByIdFromState(id: number): Post | undefined {
    return this._posts().find((p) => p.id === id);
  }

  getPostById(id: number, forceRefresh = false): Observable<Post> {
    const existing = this._posts().find((p) => p.id === id);

    if (existing && !forceRefresh) {
      return of(existing);
    }

    return this.client.getPostById(id).pipe(tap((newPost) => this.upsertPost(newPost)));
  }

  deletePost(postId: number): Observable<void> {
    return this.client
      .deletePost(postId)
      .pipe(tap(() => this._posts.update((prev) => prev.filter((p) => p.id !== postId))));
  }

  reactPost(postId: number, reaction: Reaction): Observable<void> {
    const originalPosts = this._posts();

    this._posts.update((posts) =>
      posts.map((p) => {
        if (p.id !== postId) return p;

        const isLike = reaction === 'like';

        return {
          ...p,
          isLiked: isLike,
          likesCount: p.likesCount + (isLike ? 1 : -1),
        };
      }),
    );

    return this.client.reactPost(postId, reaction).pipe(
      catchError((err) => {
        this._posts.set(originalPosts);
        throw err;
      }),
    );
  }

  commentPost(postId: number, content: string): Observable<void> {
    const originalPosts = this._posts();

    this._posts.update((posts) =>
      posts.map((p) => (p.id === postId ? { ...p, commentsCount: p.commentsCount + 1 } : p)),
    );

    return this.client.commentPost(postId, content).pipe(
      catchError((err) => {
        this._posts.set(originalPosts); // rollback
        throw err;
      }),
    );
  }

  reportPost(postId: number, reason: string): Observable<void> {
    const originalPosts = this._posts();

    this._posts.update((posts) =>
      posts.map((p) =>
        p.id === postId
          ? {
              ...p,
              isReported: true,
              reportsCount: p.reportsCount + 1,
            }
          : p,
      ),
    );

    return this.client.reportPost(postId, reason).pipe(
      catchError((err) => {
        this._posts.set(originalPosts);
        throw err;
      }),
    );
  }

  private upsertPost(newPost: Post) {
    this._posts.update((currentList) => {
      const index = currentList.findIndex((p) => p.id === newPost.id);
      if (index !== -1) {
        const updatedList = [...currentList];
        updatedList[index] = newPost;
        return updatedList;
      }
      return [newPost, ...currentList];
    });
  }
}
