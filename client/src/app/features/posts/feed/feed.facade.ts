import { Injectable, inject, signal } from '@angular/core';
import { PostsService } from '../../../core/services/posts.service';
import { Post, Reaction } from '../../../core/apis/posts/modles';
import { SessionService } from '../../../core/services/session.service';
import { catchError, EMPTY, Observable } from 'rxjs';
import { ToastService } from '../../../core/services/toast.service';

@Injectable()
export class FeedFacade {
  private readonly postsService = inject(PostsService);
  private session = inject(SessionService);
  private toast = inject(ToastService);

  // ui state
  readonly _loading = signal(false);
  readonly _loadingMore = signal(false);
  readonly _hasMore = signal(true);
  private _page = signal(0);

  readonly posts = this.postsService.posts; // Pulls from global source of truth
  readonly loading = this._loading.asReadonly();
  readonly hasMore = this._hasMore.asReadonly();

  private size = 20;

  loadFeed() {    
    this._page.set(0);
    this._fetchPosts();
  }

  loadNext() {
    if (this._loading() || !this._hasMore()) return;
    this._page.update((c) => c + 1);
    this._fetchPosts();
  }

  addPost(post: Post) {
    return this.postsService.createPost(post).pipe(
      catchError((err) => {
        return this._handleFormError(err, 'Failed to create post');
      })
    );
  }

  getPostDetails(id: number): Observable<Post> {
    return this.postsService
      .getPostById(id)
      .pipe
      // handle post not fpound
      ();
  }

  getPostById(id: number): Post | undefined {
    return this.posts().find((p) => p.id === id);
  }

  deletePost(postId: number) {
    this.postsService
      .deletePost(postId)
      .pipe(
        catchError((err) => {
          this.toast.error('Failed to delete post');
          return EMPTY;
        })
      )
      .subscribe();
  }

  reactPost(postId: number) {
    const post = this.getPostById(postId);
    if (!post) return;

    // Determine reaction: toggle like
    const reaction: Reaction = post.isLiked ? 'unlike' : 'like';

    this.postsService
      .reactPost(postId, reaction)
      .pipe(
        catchError((err) => {
          // PostsService already rolls back, we just show a toast
          this.toast.error('Failed to update reaction');
          return EMPTY;
        })
      )
      .subscribe({
        next: () => {
          const message = reaction === 'like' ? 'Post liked!' : 'Like removed!';
          this.toast.success(message);
        },
      });
  }

  addComment(postId: number, content: string) {
    this.postsService
      .commentPost(postId, content)
      .pipe(
        catchError((err) => {
          this.toast.error('Failed to add comment');
          return EMPTY;
        })
      )
      .subscribe({
        next: () => this.toast.success('Comment added!'),
      });
  }

  reportPost(postId: number, reason: string) {
    this.postsService
      .reportPost(postId, reason)
      .pipe(
        catchError((err) => {
          this.toast.error('Failed to report post');
          return EMPTY;
        })
      )
      .subscribe({
        next: () => this.toast.success('Post reported!'),
      });
  }

  private _fetchPosts() {
    this._loading.set(true);
    this.postsService.getFeed({ page: this._page(), size: this.size }).subscribe({
      next: (res) => {
        this._hasMore.set(!res.isLast);
        this._loading.set(false);
      },
      error: () => this._loading.set(false),
    });
  }
  private _handleFormError(err: any, defaultMessage: string) {
    // Here you can inspect err for validation errors from backend
    const message = err?.message || defaultMessage;
    this.toast.error(message);
    return EMPTY;
  }
}
