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
import { CommonModule } from '@angular/common';
import { BlogEditor } from '../editor/blog-editor/blog-editor';
import { FeedFacade } from './feed.facade';
import { FeedSkeleton } from '../../../shared/components/feed-skeleton/feed-skeleton';
import { UserProfile } from '../../../shared/components/user-profile/user-profile';
import { Post } from '../../../core/apis/posts/modles';
import { EmptyState } from '../../../shared/components/empty-state/empty-state';
import { Router } from '@angular/router';
import { PostCard } from '../../../shared/components/post-card/post-card';
import { SessionService } from '../../../core/services/session.service';

@Component({
  selector: 'app-feed',
  standalone: true,
  imports: [CommonModule, FeedSkeleton, PostCard, BlogEditor, EmptyState],
  templateUrl: './feed.html',
  styleUrls: ['./feed.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Feed implements OnInit, AfterViewInit, OnDestroy {
  private readonly facade = inject(FeedFacade);
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);

  readonly user = this.session.getUser();
  readonly posts = this.facade.posts;
  readonly loading = this.facade.loading;
  readonly loadingMore = this.facade.loadingMore;
  readonly hasMore = this.facade.hasMore;

  @ViewChild('sentinel') private sentinel!: ElementRef;

  private observer!: IntersectionObserver;

  ngOnInit() {
    this.facade.loadFeed();
  }

  ngAfterViewInit() {
    this.observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && !this.loadingMore()) {
          this.facade.loadNext();
        }
      },
      { rootMargin: '400px' }
    );

    this.observer.observe(this.sentinel.nativeElement);
  }

  ngOnDestroy() {
    this.observer?.disconnect();
  }

  trackByPostId(_: number, post: Post) {
    return post.id;
  }

  goToPost(postId: number) {
    this.router.navigate(['/posts/', postId]);
  }
}
