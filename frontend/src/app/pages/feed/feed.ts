import { Component, inject, OnInit, signal } from '@angular/core';
import { PostService } from '../../core/services/post.service';
import { PostCard } from '../../shared/components/post-card/post-card';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { NotificationService } from '../../core/services/notification.service';
import { PageRequest } from '../../shared/models/pagination.model';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-feed',
  standalone: true,
  imports: [PostCard, MatButtonModule, MatProgressSpinnerModule,MatIcon],
  templateUrl: './feed.html',
  styleUrl: './feed.scss',
})
export class Feed implements OnInit {
  public postService = inject(PostService);
  private notify = inject(NotificationService);

  currentPage = signal(0);
  pageSize = signal(10);
  isLastPage = signal(false);

  ngOnInit() {
    this.loadInitial();
  }

  loadInitial() {
    this.currentPage.set(0);
    this.fetchFeed(false);
  }

  loadMore() {
    this.currentPage.update((p) => p + 1);
    this.fetchFeed(true);
  }

  private fetchFeed(append: boolean) {
    const params: PageRequest = {
      page: this.currentPage(),
      size: this.pageSize(),
      sort: 'createdAt,desc',
    };

    this.postService.getFeed(params, append).subscribe((res) => {
      this.isLastPage.set(res.isLast);
    });
  }

  handleComment(postId: number, content: string) {
    this.postService.comment(postId, content);
  }

  handleReaction(postId: number) {
    this.postService.react(postId);
  }

  handleDelete(postId: number) {
    // need to show a popup instean of the browser popup
    if (confirm('Delete this post?')) {
      this.postService.delete(postId).subscribe({
        next: () => this.notify.showSuccess('Post removed'),
        error: () => this.notify.showError('Could not delete post'),
      });
    }
  }

  handleReport(postId: number, reason: string) {
    this.postService.report(postId, reason).subscribe({
      next: () => this.notify.showSuccess('Report submitted'),
    });
  }
}
