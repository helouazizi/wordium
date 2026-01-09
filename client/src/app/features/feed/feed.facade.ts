import { Injectable, inject } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { PostsService } from '../../core/services/posts.service';
import { Post } from '../../core/apis/posts/modles';
import { PageResponse } from '../../shared/models/pagination.model';
import { PageRequest } from '../../shared/models/page-request.model';

@Injectable()
export class FeedFacade {
  private readonly postsService = inject(PostsService);

  private readonly postsSubject = new BehaviorSubject<Post[]>([]);
  readonly posts$ = this.postsSubject.asObservable();

  private readonly loadingSubject = new BehaviorSubject<boolean>(false);
  readonly loading$ = this.loadingSubject.asObservable();

  private readonly loadingMoreSubject = new BehaviorSubject<boolean>(false);
  readonly loadingMore$ = this.loadingMoreSubject.asObservable();

  private readonly hasMoreSubject = new BehaviorSubject<boolean>(true);
  readonly hasMore$ = this.hasMoreSubject.asObservable();

  private page = 0;
  private size = 10;

  loadFeed(): void {
    this.page = 0;
    this.hasMoreSubject.next(true);
    this.fetchPosts(false);
  }

  loadNext(): void {
    if (!this.hasMoreSubject.value || this.loadingMoreSubject.value) return;

    this.page++;
    this.fetchPosts(true);
  }

  private fetchPosts(append: boolean): void {
    if (append) {
      this.loadingMoreSubject.next(true);
    } else {
      this.loadingSubject.next(true);
    }

    const params: PageRequest = { page: this.page, size: this.size };

    this.postsService
      .getFeed(params)
      .pipe(
        finalize(() => {
          this.loadingSubject.next(false);
          this.loadingMoreSubject.next(false);
        })
      )
      .subscribe({
        next: (res: PageResponse<Post>) => {
          const current = this.postsSubject.value;
          const newPosts = append ? [...current, ...res.data] : res.data;
          this.postsSubject.next(newPosts);
          this.hasMoreSubject.next(res.hasNext);
        },
        error: () => {
          this.hasMoreSubject.next(false);
        },
      });
  }
}
