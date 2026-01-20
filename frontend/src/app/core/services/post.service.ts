// src/app/core/services/post.service.ts
import { inject, Injectable, signal, computed } from '@angular/core';
import { CreatePostRequest, Post, Reaction, SignatureResponse } from '../apis/posts/post.model';
import { catchError, finalize, Observable, of, switchMap, tap, throwError } from 'rxjs';
import { PostsClient } from '../apis/posts/post-client';
import { PageRequest } from '../../shared/models/pagination.model';

@Injectable({ providedIn: 'root' })
export class PostService {
  private readonly client = inject(PostsClient);

  private _posts = signal<Post[]>([]);
  private _loading = signal<boolean>(false);

  public posts = this._posts.asReadonly();
  public loading = this._loading.asReadonly();
  public isEmpty = computed(() => this._posts().length === 0 && !this._loading());

  getFeed(params: PageRequest, append: boolean = false) {
    this._loading.set(true);
    return this.client.feed(params).pipe(
      finalize(() => this._loading.set(false)),
      tap((response) => {
        if (append) {
          this._posts.update((current) => [...current, ...response.data]);
        } else {
          this._posts.set(response.data);
        }
      }),
    );
  }

  createPost(post: CreatePostRequest): Observable<Post> {
    return this.client
      .createPost(post)
      .pipe(tap((newPost) => this._posts.update((prev) => [newPost, ...prev])));
  }
  getPostById(id: number): Post | undefined {
    return this.posts().find((p) => p.id === id);
  }

  react(postId: number) {
    const previousState = this._posts();

    const post = previousState.find((p) => p.id === postId);
    if (!post) return of(null);

    const isAddingLike = !post.isLiked;

    this._posts.update((posts) =>
      posts.map((p) => {
        if (p.id === postId) {
          return {
            ...p,
            isLiked: isAddingLike,
            likesCount: isAddingLike ? p.likesCount + 1 : Math.max(0, p.likesCount - 1),
          };
        }
        return p;
      }),
    );

    const reactionType: Reaction = isAddingLike ? 'like' : 'unlike';

    return this.client.reactPost(postId, reactionType).pipe(
      catchError((error) => {
        this._posts.set(previousState);
        return throwError(() => error);
      }),
    );
  }

  delete(postId: number) {
    return this.client.deletePost(postId).pipe(
      tap(() => {
        this._posts.update((posts) => posts.filter((p) => p.id !== postId));
      }),
    );
  }

  comment(postId: number, content: string) {
    return this.client.commentPost(postId, content);
  }

  report(postId: number, reason: string) {
    return this.client.reportPost(postId, reason);
  }

  getSignature(): Observable<SignatureResponse> {
    return this.client.getSignature();
  }

  uploadToCloudinary(file: File, sigData: any) {
    return this.client.uploadToCloudinary(file, sigData);
  }

  uploadImage(file: File): Observable<any> {
    return this.getSignature().pipe(switchMap((res) => this.uploadToCloudinary(file, res.data)));
  }
}
