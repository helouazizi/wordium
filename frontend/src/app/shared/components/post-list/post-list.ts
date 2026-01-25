import {
  AfterViewInit,
  Component,
  ElementRef,
  inject,
  input,
  OnInit,
  signal,
  ViewChild,
} from '@angular/core';
import { PostService } from '../../../core/services/post.service';
import { AuthService } from '../../../core/services/auth.service';
import { NotificationService } from '../../../core/services/notification.service';
import { PageRequest, PageResponse } from '../../models/pagination.model';
import { Post, Reaction } from '../../../core/apis/posts/post.model';
import { PostCard } from '../post-card/post-card';
import { MatProgressSpinner, MatSpinner } from '@angular/material/progress-spinner';
import { MatIcon } from '@angular/material/icon';
import { Router } from '@angular/router';
import { MatButtonToggle } from "@angular/material/button-toggle";

export type PostListSource = 'admin' | 'feed' | 'profile' | 'detail' | 'bookmarks';

@Component({
  selector: 'app-post-list',
  imports: [PostCard, MatProgressSpinner, MatSpinner, MatIcon],
  templateUrl: './post-list.html',
  styleUrl: './post-list.scss',
})
export class PostList implements OnInit, AfterViewInit {
  @ViewChild('scrollAnchor') anchor!: ElementRef<HTMLElement>;
  private observer?: IntersectionObserver;

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

  posts = signal<Post[]>([]);
  initialLoading = signal(false);
  pageLoading = signal(false);

  isEmpty = () => !this.initialLoading() && this.posts().length === 0;
  endReached = () => this.isLastPage() && !this.initialLoading() && this.posts().length > 0;

  constructor() {}

  ngOnInit() {
    if (!this.source()) {
      throw new Error('PostList: source input is required');
    }
    this.loadInitial();
  }

  ngAfterViewInit() {
    if (!this.anchor) return;

    this.observer = new IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting) this.loadMore();
      },
      { rootMargin: '200px' },
    );
    this.observer.observe(this.anchor.nativeElement);
  }

  loadInitial() {
    this.currentPage.set(0);
    this.posts.set([]);
    this.isLastPage.set(false);
    this.initialLoading.set(true);
    this.fetchData(false);
  }

  loadMore() {
    if (this.pageLoading() || this.isLastPage()) return;
    this.currentPage.update((p) => p + 1);
    this.pageLoading.set(true);
    this.fetchData(true);
  }

  private fetchData(append: boolean) {
    const params: PageRequest = {
      page: this.currentPage(),
      size: 10,
      sort: 'createdAt,desc',
    };

    const apiCall = this.resolveApiCall(params);

    apiCall.subscribe({
      next: (res: PageResponse<Post>) => {
        this.posts.update((existing) => (append ? [...existing, ...res.data] : res.data));
        this.isLastPage.set(res.isLast);
        this.initialLoading.set(false);
        this.pageLoading.set(false);
      },
      error: () => {
        this.initialLoading.set(false);
        this.pageLoading.set(false);
        this.notify.showError('Error loading posts');
      },
    });
  }

  private resolveApiCall(params: PageRequest) {
    switch (this.source()) {
      case 'profile':
        return this.postService.getUserPosts(this.userId()!, params);
      case 'admin':
        return this.postService.getAllPosts(params);
      case 'bookmarks':
        return this.postService.getBookmarks(params);
      default:
        return this.postService.getFeed(params);
    }
  }

  addComment(postId: number, content: string) {
    this.postService.addComment(postId, content);
  }

 reactToPost(postId: number) {
  this.updatePost(postId, (p) => {
    const newIsLiked = !p.isLiked;
    const newLikeCount = newIsLiked ? p.likesCount + 1 : p.likesCount - 1;

    return {
      ...p,
      isLiked: newIsLiked,
      likesCount: newLikeCount,
    };
  });

  const post = this.posts().find((p) => p.id === postId);
  if (!post) return;

  const reaction: Reaction = post.isLiked ? 'like' : 'unlike';

  this.postService.reactToPost(postId, reaction).subscribe({
    error: () => {
      this.updatePost(postId, (p) => ({
        ...p,
        isLiked: !p.isLiked,
        likesCount: p.isLiked ? p.likesCount + 1 : p.likesCount - 1,
      }));
      this.notify.showError('Could not update reaction');
    },
  });
}


  deletePost(postId: number) {
    this.postService.deletePost(postId).subscribe({
      next: () => this.notify.showSuccess('Post removed'),
      error: () => this.notify.showError('Could not delete post'),
    });
  }

  reportPost(postId: number, reason: string) {
    this.postService.reportPost({ id: postId, type: 'post', reason }).subscribe({
      next: () => this.notify.showSuccess('Report submitted'),
    });
  }

  private updatePost(postId: number, updater: (p: Post) => Post) {
    this.posts.update((posts) => posts.map((p) => (p.id === postId ? updater(p) : p)));
  }

  getEndMessage() {
    const src = this.source();
    switch (src) {
      case 'feed':
        return {
          icon: 'group_add',
          title: 'You’re all caught up',
          message: 'Follow more people to see fresh posts in your feed.',
          action: 'Find people',
        };
      case 'bookmarks':
        return {
          icon: 'bookmark',
          title: 'No more bookmarks',
          message: 'Save posts to see them here later.',
        };
      case 'profile':
        return {
          icon: 'person',
          title: 'End of posts',
          message: 'This user hasn’t posted anything else yet.',
        };
      case 'admin':
        return {
          icon: 'check_circle',
          title: 'All posts loaded',
          message: 'You’ve reached the end of the list.',
        };
      default:
        return {
          icon: 'check_circle',
          title: 'All posts loaded',
          message: 'You’ve reached the end of the list.',
        };
    }
  }

  goToDiscover() {
    this.router.navigate(['/discover']);
  }

  get endMessage() {
    return this.getEndMessage();
  }
}
