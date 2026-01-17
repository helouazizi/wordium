import { Injectable, computed, inject, signal } from '@angular/core';
import { PostsService } from '../../../core/services/posts.service';
import { Reaction } from '../../../core/apis/posts/modles';
import { catchError, EMPTY, Observable } from 'rxjs';
import { ToastService } from '../../../core/services/toast.service';

@Injectable()
export class PostDetailsFacade {
  private postsService = inject(PostsService);
  private toast = inject(ToastService);

  private _loading = signal(false);
  private _postId = signal<number | null>(null);
  private _error = signal<boolean>(false);

  readonly loading = this._loading.asReadonly();
  readonly error = this._error.asReadonly();

  readonly post = computed(() => {
    const id = this._postId();
    return this.postsService.getPostByIdFromState(id!);
  });

  setPostId(id: number) {
    this._postId.set(id);
    this._error.set(false);

    if (!this.post()) {
      this._loading.set(true);
      this.postsService.getPostById(id).subscribe({
        next: () => this._loading.set(false),
        error: () => {
          this._loading.set(false);
          this._error.set(true);
          this.toast.error('Post not found');
          
        },
      });
    }
  }

  delete() {
    const id = this._postId();
    if (id) return this.postsService.deletePost(id);
    return EMPTY;
  }

  reactPost(postId: number) {
    const post = this.postsService.getPostByIdFromState(postId);
    if (!post) return;

    const reaction: Reaction = post.isLiked ? 'unlike' : 'like';

    this.postsService
      .reactPost(postId, reaction)
      .pipe(
        catchError((err) => {
          this.toast.error('Failed to update reaction');
          return EMPTY;
        }),
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
        }),
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
        }),
      )
      .subscribe({
        next: () => this.toast.success('Post reported!'),
      });
  }
}
