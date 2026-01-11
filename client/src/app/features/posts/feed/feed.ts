import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  inject,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { AsyncPipe, CommonModule } from '@angular/common';
import { BlogEditor } from '../editor/blog-editor/blog-editor';

import { FeedFacade } from './feed.facade';
import { Subject } from 'rxjs';
import { FeedSkeleton } from '../../../shared/components/feed-skeleton/feed-skeleton';
import { UserProfile } from '../../../shared/components/user-profile/user-profile';
import { Post } from '../../../core/apis/posts/modles';

@Component({
  selector: 'app-feed',
  standalone: true,
  imports: [CommonModule, FeedSkeleton, UserProfile, AsyncPipe ,BlogEditor],
  templateUrl: './feed.html',
  styleUrls: ['./feed.scss'],
  providers: [FeedFacade],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Feed implements OnInit, AfterViewInit, OnDestroy {
  private readonly facade = inject(FeedFacade);
  private readonly destroy$ = new Subject<void>();

  readonly posts$ = this.facade.posts$;
  readonly loading$ = this.facade.loading$;
  readonly loadingMore$ = this.facade.loadingMore$;
  readonly hasMore$ = this.facade.hasMore$;

  @ViewChild('sentinel') private sentinel!: ElementRef;

  private isLoadingMore = false;

  ngOnInit(): void {
    this.facade.loadFeed();
    this.loadingMore$.subscribe((val) => (this.isLoadingMore = val));
  }

  ngAfterViewInit(): void {
    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && !this.isLoadingMore) {
          this.facade.loadNext();
        }
      },
      { rootMargin: '400px' }
    );

    observer.observe(this.sentinel.nativeElement);

    this.destroy$.subscribe(() => observer.disconnect());
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  trackByPostId(_: number, post: Post): number {
    return post.id;
  }
}
