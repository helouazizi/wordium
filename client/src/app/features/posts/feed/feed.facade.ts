import { Injectable, inject, signal } from '@angular/core';
import { PostsService } from '../../../core/services/posts.service';
import { Post, Reaction } from '../../../core/apis/posts/modles';
import { PageRequest } from '../../../shared/models/page-request.model';
import { SessionService } from '../../../core/services/session.service';

@Injectable({ providedIn: 'root' })
export class FeedFacade {
  private readonly postsService = inject(PostsService);

  private session = inject(SessionService);
  private user = this.session.getUser();
  readonly posts = signal<Post[]>([]);
  readonly loading = signal(false);
  readonly loadingMore = signal(false);
  readonly hasMore = signal(true);

  private page = 0;
  private size = 10;

  loadFeed() {
    this.page = 0;
    this.hasMore.set(true);
    this.fetch(false);
  }

  loadNext() {
    if (!this.hasMore() || this.loadingMore()) return;

    this.page++;
    this.fetch(true);
  }

  private fetch(append: boolean) {
    append ? this.loadingMore.set(true) : this.loading.set(true);

    const params: PageRequest = { page: this.page, size: this.size };

    this.postsService.getFeed(params).subscribe({
      next: (res) => {
        this.posts.update((current) => (append ? [...current, ...res.data] : res.data));

        this.hasMore.set(res.hasNext);
      },
      error: () => {
        this.hasMore.set(false);
      },
      complete: () => {
        this.loading.set(false);
        this.loadingMore.set(false);
        console.log(this.posts());
      },
    });
  }

  addPost(post: Post) {
    post.actor = this.user!;
    this.posts.update((p) => [post, ...p]);
  }

  getPostById(id: number): Post | undefined {
    return this.posts().find((p) => p.id === id);
  }

  deletePost(postId: number) {
    this.postsService.deletePost(postId).subscribe({
      error: () => {
        // If delete failed, reload feed or handle error
        console.error('Failed to delete post');
      },
    });

    this.posts.update((p) => p.filter((post) => post.id !== postId));
  }

  reactPost(postId: number, reaction: Reaction) {
    const post = this.getPostById(postId);
    if (!post) return;

    // Optimistic UI update
    if (reaction === 'like') {
      post.isLiked = true;
      post.likesCount++;
    } else {
      post.isLiked = false;
      post.likesCount--;
    }

    this.posts.update((p) => [...p]); // trigger signal

    this.postsService.reactPost(postId, reaction).subscribe({
      error: () => {
        // rollback on error
        if (reaction === 'like') {
          post.isLiked = false;
          post.likesCount--;
        } else {
          post.isLiked = true;
          post.likesCount++;
        }
        this.posts.update((p) => [...p]);
      },
    });
  }
  addComment(postId: number, content: string) {
    const post = this.getPostById(postId);
    if (!post) return;

    this.postsService.commentPost(postId, content).subscribe({
      next: () => {
        post.commentsCount = (post.commentsCount || 0) + 1;
        this.posts.update((p) => [...p]);
      },
      error: () => console.error('Failed to add comment'),
    });
  }

  reportPost(postId: number, content: string) {
    this.postsService.reportPost(postId, content).subscribe({
      next: () => {
        const post = this.getPostById(postId);
        if (post) {
          post.reportsCount = (post.reportsCount || 0) + 1;
          this.posts.update((p) => [...p]);
        }
      },
      error: () => console.error('Failed to report post'),
    });
  }
}
