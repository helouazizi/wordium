import { Component, inject, OnInit, signal } from '@angular/core';
import { PostService } from '../../core/services/post.service';
import { PostCard } from '../../shared/components/post-card/post-card';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { NotificationService } from '../../core/services/notification.service';
import { PageRequest } from '../../shared/models/pagination.model';
import { MatIcon } from '@angular/material/icon';
import { AuthService } from '../../core/services/auth.service';
import { PostList } from '../../shared/components/post-list/post-list';

@Component({
  selector: 'app-feed',
  standalone: true,
  imports: [PostList, MatButtonModule, MatProgressSpinnerModule, ],
  templateUrl: './feed.html',
  styleUrl: './feed.scss',
})
export class Feed implements OnInit {
  // public postService = inject(PostService);
  // private notify = inject(NotificationService);
  // private auth = inject(AuthService);
  // user = this.auth.user();

  // currentPage = signal(0);
  // pageSize = signal(10);
  // isLastPage = signal(false);

  ngOnInit() {
    // this.loadInitial();
  }

  // loadInitial() {
  //   this.currentPage.set(0);
  //   this.fetchFeed(false);
  // }

  // loadMore() {
  //   this.currentPage.update((p) => p + 1);
  //   this.fetchFeed(true);
  // }

  // private fetchFeed(append: boolean) {
  //   const params: PageRequest = {
  //     page: this.currentPage(),
  //     size: this.pageSize(),
  //     // sort: 'createdAt,desc',
  //   };

  //   this.postService.getFeed(params, append).subscribe((res) => {
  //     this.isLastPage.set(res.isLast);
  //   });
  // }

  // handleComment(postId: number, content: string) {
  //   this.postService.addComment(postId, content);
  // }

  // handleReaction(postId: number) {
  //   this.postService.reactToPost(postId);
  // }

  // handleDelete(postId: number) {
  //   // need to show a popup instean of the browser popup
  //   if (confirm('Delete this post?')) {
  //     this.postService.deletePost(postId).subscribe({
  //       next: () => this.notify.showSuccess('Post removed'),
  //       error: () => this.notify.showError('Could not delete post'),
  //     });
  //   }
  // }

  // handleReport(postId: number, reason: string) {
  //   this.postService.reportPost({ id: postId, type: 'post', reason: reason }).subscribe({
  //     next: () => this.notify.showSuccess('Report submitted'),
  //   });
  // }
}
