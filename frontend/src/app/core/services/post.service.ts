import { inject, Injectable, signal, computed } from '@angular/core';
import {
  CreatePostRequest,
  Post,
  Reaction,
  ReportType,
  SignatureResponse,
} from '../apis/posts/post.model';
import { catchError, finalize, Observable, of, switchMap, tap, throwError } from 'rxjs';
import { PostsClient } from '../apis/posts/post-client';
import { PageRequest } from '../../shared/models/pagination.model';

@Injectable({ providedIn: 'root' })
export class PostService {
  private readonly client = inject(PostsClient);

  // private _posts = signal<Post[]>([]);
  // private _loading = signal<boolean>(false);

  // public posts = this._posts.asReadonly();
  // public loading = this._loading.asReadonly();
  // public isEmpty = computed(() => this._posts().length === 0 && !this._loading());

  getFeed(params: PageRequest) {
    // this._loading.set(true);
    return this.client.feed(params)
    // .pipe(
      // finalize(() => this._loading.set(false)),
    //   tap((response) => {
    //     if (append) {
    //       this._posts.update((current) => [...current, ...response.data]);
    //     } else {
    //       this._posts.set(response.data);
    //     }
    //   }),
    // );
  }
  getUserPosts(userid: number, params: PageRequest) {
    // this._loading.set(true);
    return this.client.getUserPosts(userid, params)
    // .pipe(
    //   // finalize(() => this._loading.set(false)),
    //   tap((response) => {
    //     if (append) {
    //       this._posts.update((current) => [...current, ...response.data]);
    //     } else {
    //       this._posts.set(response.data);
    //     }
    //   }),
    // );
  }

  getAllPosts(params: PageRequest) {
    // this._loading.set(true);
    return this.client.getAllPosts(params)
    // .pipe(
    //   finalize(() => this._loading.set(false)),
    //   tap((response) => {
    //     if (append) {
    //       this._posts.update((current) => [...current, ...response.data]);
    //     } else {
    //       this._posts.set(response.data);
    //     }
    //   }),
    // );
  }
  getBookmarks(params: PageRequest) {
    // this._loading.set(true);
    return this.client.getBookmarks(params)
    // .pipe(
    //   finalize(() => this._loading.set(false)),
    //   tap((response) => {
    //     if (append) {
    //       this._posts.update((current) => [...current, ...response.data]);
    //     } else {
    //       this._posts.set(response.data);
    //     }
    //   }),
    // );
  }

  fetchPostById(id: number): Observable<Post> {
    return this.client.getPostById(id);
  }
  createPost(post: CreatePostRequest): Observable<Post> {
    return this.client
      .createPost(post)
      // .pipe(tap((newPost) => this._posts.update((prev) => [newPost, ...prev])));
  }
  // getPostById(id: number): Post | undefined {
  //   return this.posts().find((p) => p.id === id);
  // }

  reactToPost(postId: number,reaction : Reaction)  {
    // const previousState = this._posts();

    // const post = previousState.find((p) => p.id === postId);
    // if (!post) return of(null);

    // const isAddingLike = !post.isLiked;

    // this._posts.update((posts) =>
    //   posts.map((p) => {
    //     if (p.id === postId) {
    //       return {
    //         ...p,
    //         isLiked: isAddingLike,
    //         likesCount: isAddingLike ? p.likesCount + 1 : Math.max(0, p.likesCount - 1),
    //       };
    //     }
    //     return p;
    //   }),
    // );

    // const reactionType: Reaction = isAddingLike ? 'like' : 'unlike';

    return this.client.reactPost(postId, reaction)
    // .pipe(
    //   catchError((error) => {
    //     this._posts.set(previousState);
    //     return throwError(() => error);
    //   }),
    // );
  }

  deletePost(postId: number) {
    return this.client.deletePost(postId)
    // .pipe(
    //   tap(() => {
    //     this._posts.update((posts) => posts.filter((p) => p.id !== postId));
    //   }),
    // );
  }

  addComment(postId: number, content: string) {
    return this.client.addComment(postId, content);
  }

  reportPost(event: { id: number; type: ReportType; reason: string }) {
    return this.client.reportPost(event.id, event.reason);
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
