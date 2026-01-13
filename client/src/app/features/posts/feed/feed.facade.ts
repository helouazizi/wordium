import { Injectable, inject, signal } from '@angular/core';
import { PostsService } from '../../../core/services/posts.service';
import { Post } from '../../../core/apis/posts/modles';
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
}
