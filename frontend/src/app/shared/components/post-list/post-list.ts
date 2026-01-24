import { Component, effect, inject, input, OnInit, signal } from '@angular/core';
import { PostService } from '../../../core/services/post.service';
import { AuthService } from '../../../core/services/auth.service';
import { NotificationService } from '../../../core/services/notification.service';
import { Router } from '@angular/router';
import { PageRequest, PageResponse } from '../../models/pagination.model';
import { Post } from '../../../core/apis/posts/post.model';
import { PostCard } from '../post-card/post-card';

export type PostListSource = 'admin' | 'feed' | 'profile' | 'bookmarks';

@Component({
  selector: 'app-post-list',
  imports: [PostCard],
  templateUrl: './post-list.html',
  styleUrl: './post-list.scss',

})
export class PostList implements OnInit {
  private postService = inject(PostService);
  private auth = inject(AuthService);
  private notify = inject(NotificationService);
  private router = inject(Router);

  source = input.required<PostListSource>();

  emptyMessage = input<string>('No posts found here yet.');

  user = this.auth.user();
  userId = input<number>(this.user?.id!);
  currentPage = signal(0);
  isLastPage = signal(false);

  posts = signal<any[]>([]);
  loading = signal(false);

  constructor() {
    effect(() => {
      this.source();
      this.userId();
      this.loadInitial();
    });
  }
  ngOnInit() {}

  loadInitial() {
    this.currentPage.set(0);
    this.posts.set([]);
    this.fetchData(false);
  }

  loadMore() {
    if (this.loading() || this.isLastPage()) return;
    this.currentPage.update((p) => p + 1);
    this.fetchData(true);
  }

  private fetchData(append: boolean) {
    const params: PageRequest = {
      page: this.currentPage(),
      size: 10,
      sort: 'createdAt,desc',
    };

    const apiCall = this.resolveApiCall(params, append);

    apiCall.subscribe({
      next: (res: PageResponse<Post>) => {
        this.posts.set(res.data)
        this.isLastPage.set(res.isLast);
        console.log(this.posts(),"posts");
        
      },
      error: () => {
        this.notify.showError('Error loading posts');
      },
    });
  }

  private resolveApiCall(params: PageRequest, append: boolean) {
    switch (this.source()) {
      case 'profile':
        return this.postService.getUserPosts(this.userId()!, params, append);
      case 'admin':
        return this.postService.getAllPosts(params, append);
      case 'bookmarks':
        return this.postService.getBookmarks(params, append);
      default:
        return this.postService.getFeed(params, append);
    }
  }

  addComment(postId: number, content: string) {
    this.postService.addComment(postId, content);
  }
  fetchPostComments(postId: number, content: string) {
    this.postService.addComment(postId, content);
  }
  reactToPost(postId: number) {
    this.postService.reactToPost(postId);
  }

  deletePost(postId: number) {
    this.postService.deletePost(postId).subscribe({
      next: () => this.notify.showSuccess('Post removed'),
      error: () => this.notify.showError('Could not delete post'),
    });
  }

  reportPost(postId: number, reason: string) {
    this.postService.reportPost({ id: postId, type: 'post', reason: reason }).subscribe({
      next: () => this.notify.showSuccess('Report submitted'),
    });
  }
}
