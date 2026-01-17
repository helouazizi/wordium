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
import { EmptyState } from '../../../shared/components/empty-state/empty-state';
import { Router } from '@angular/router';
import { PostCard } from '../../../shared/components/post-card/post-card';


@Component({
  selector: 'app-feed',
  standalone: true,
  imports: [CommonModule, FeedSkeleton, PostCard, BlogEditor, EmptyState],
  templateUrl: './feed.html',
  styleUrls: ['./feed.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [FeedFacade], // Component-scoped facade
})
export class Feed implements OnInit, AfterViewInit, OnDestroy {
  private readonly facade = inject(FeedFacade);
  private readonly router = inject(Router);

  readonly posts = this.facade.posts;
  readonly loading = this.facade.loading;
  readonly hasMore = this.facade.hasMore;

  @ViewChild('sentinel') private sentinel!: ElementRef;

  private observer!: IntersectionObserver;

  ngOnInit() {
    this.facade.loadFeed();
  }

  ngAfterViewInit() {
    this.observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && this.facade.hasMore()) {
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
  goToPost(postId: number) {
    this.router.navigate(['/posts/', postId]);
  }

  onLike(postId: number) {
    this.facade.reactPost(postId);
  }

  onComment(postId: number, content: string) {
    this.facade.addComment(postId, content);
  }

  onReport(postId: number, reason: string) {
    this.facade.reportPost(postId, reason);
  }

  onDelete(postId: number) {
    this.facade.deletePost(postId);
  }


}
