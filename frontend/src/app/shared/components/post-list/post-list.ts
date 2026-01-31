import {
  AfterViewInit,
  Component,
  ElementRef,
  EventEmitter,
  inject,
  input,
  OnInit,
  Output,
  signal,
  ViewChild,
} from '@angular/core';
import { PostService } from '../../../core/services/post.service';
import { AuthService } from '../../../core/services/auth.service';
import { NotificationService } from '../../../core/services/notification.service';
import { PageRequest, PageResponse } from '../../models/pagination.model';
import { Comment, Post, Reaction, ReportType } from '../../../core/apis/posts/post.model';
import { PostCard } from '../post-card/post-card';
import { MatProgressSpinner, MatSpinner } from '@angular/material/progress-spinner';
import { MatIcon } from '@angular/material/icon';
import { ActivatedRoute, Router } from '@angular/router';
import { EMPTY, map, Observable } from 'rxjs';
import { NotFound } from '../not-found/not-found';

export type PostListSource = 'admin' | 'feed' | 'profile' | 'detail' | 'bookmarks';

@Component({
  selector: 'app-post-list',
  imports: [PostCard, MatProgressSpinner, MatSpinner, MatIcon, NotFound],
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
  private route = inject(ActivatedRoute);

  source = input.required<PostListSource>();
  @Output() postDeleted = new EventEmitter<void>();

  emptyMessage = input<string>('No posts found here yet.');
  user = this.auth.user();

  currentPage = signal(0);
  isLastPage = signal(false);

  posts = signal<Post[]>([]);
  initialLoading = signal(false);
  pageLoading = signal(false);

  postComments = signal<Comment[]>([]);
  commentsPage = signal(0);
  commentsLastPage = signal(false);
  commentsLoading = signal(false);

  err = signal<string | null>(null);
  isEmpty = () => !this.initialLoading() && this.posts().length === 0;
  endReached = () => this.isLastPage() && !this.initialLoading() && this.posts().length > 0;

  constructor() {}

  ngOnInit() {
    if (!this.source()) {
      throw new Error('PostList: source input is required');
    }
    if (this.source() != 'detail') {
      this.loadInitial();
    } else {
      this.loadInitialComments();
    }
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
        this.posts.update((existing) =>
          append
            ? Array.from(new Map([...existing, ...res.data].map((p) => [p.id, p])).values())
            : res.data,
        );
        console.log(this.posts(), 'postsss');

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

  private resolveApiCall(params: PageRequest): Observable<PageResponse<Post>> {
    switch (this.source()) {
      case 'profile':
        const idParamm = this.route.snapshot.paramMap.get('id');
        const idd = Number(idParamm);

        if (!idParamm || isNaN(idd)) {
          this.err.set('User Not Found');
          this.initialLoading.set(false);
          return EMPTY;
        }
        return this.postService.getUserPosts(idd, params);

      case 'admin':
        return this.postService.getAllPosts(params);

      case 'detail':
        const idParam = this.route.snapshot.paramMap.get('id');
        const id = Number(idParam);

        if (!idParam || isNaN(id)) {
          this.err.set('Post Not Found');
          this.initialLoading.set(false);
          return EMPTY;
        }

        return this.postService.getPostById(id).pipe(
          map((post) => ({
            data: [post],
            page: 0,
            size: 1,
            totalElements: 1,
            totalPages: 1,
            isLast: true,
            isFirst: true,
            hasNext: false,
            hasPrevious: false,
          })),
        );

      default:
        return this.postService.getFeed(params);
    }
  }

  addComment(postId: number, content: string) {
    this.updatePost(postId, (p) => ({
      ...p,
      commentsCount: (p.commentsCount || 0) + 1,
    }));

    this.postService.addComment(postId, content).subscribe({
      next: (res) => {
        this.postComments.update((prev) => [res, ...prev]);
        this.notify.showSuccess('New comment added');
      },
      error: () => {
        this.updatePost(postId, (p) => ({
          ...p,
          commentsCount: (p.commentsCount || 0) - 1,
        }));
        this.notify.showError('Failed to add comment');
      },
    });
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

  handleVisibility(postId: number) {
    const post = this.posts().find((p) => p.id === postId);
    if (!post) return;

    const wasFlagged = post.isFlagged ?? false;

    this.updatePost(postId, (p) => ({ ...p, isFlagged: !wasFlagged }));

    const action$ = wasFlagged
      ? this.postService.unflagPost(postId)
      : this.postService.flagPost(postId);

    action$.subscribe({
      next: () => {
        const msg = wasFlagged ? 'Post unflagged' : 'Post flagged';
        this.notify.showSuccess(msg);
      },
      error: () => {
        this.updatePost(postId, (p) => ({ ...p, isFlagged: wasFlagged }));
        this.notify.showError('Failed to update post status');
      },
    });
  }

  deletePost(postId: number) {
    this.postService.deletePost(postId).subscribe({
      next: () => {
        this.posts.update((prev) => prev.filter((p) => p.id !== postId));
        this.notify.showSuccess('Post removed');
        this.postDeleted.emit();
      },
      error: () => this.notify.showError('Could not delete post'),
    });
  }

  deleteComment(postId: number, commentId: number) {
    this.postService.deleteComment(postId, commentId).subscribe({
      next: () => {
        this.postComments.update((prev) => prev.filter((c) => c.id !== commentId));
        this.notify.showSuccess('Comment removed');
      },
      error: (err) => {
        this.notify.showError(err.error.title);
      },
    });
  }

  handleUpdatePost(post: Post) {
    this.router.navigate(['write'], {
      state: { post },
    });
  }

  reportPost(id: number, type: ReportType, reason: string) {
    if (type === 'post') {
      this.updatePost(id, (p) => ({
        ...p,
        reportsCount: (p.reportsCount || 0) + 1,
      }));
    }
    let request$;
    if (type === 'post') {
      request$ = this.postService.reportPost(id, reason);
    } else if (type === 'user') {
      request$ = this.postService.reportUser(id, reason);
    } else {
      console.error('Unknown report type:', type);
      return;
    }

    request$.subscribe({
      next: () => {
        this.notify.showSuccess('New report added');
      },
      error: (err) => {
        if (type === 'post') {
          this.updatePost(id, (p) => ({
            ...p,
            reportsCount: (p.reportsCount || 0) - 1,
          }));
        }
        this.notify.showError(err.error.detail);
      },
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

  loadMoreComments() {
    if (this.commentsLoading() || this.commentsLastPage()) return;

    this.commentsPage.update((p) => p + 1);
    this.commentsLoading.set(true);

    this.fetchComments(true);
  }

  loadInitialComments() {
    const postId = this.getPostId();
    if (!postId) {
      this.err.set('Post Not Found');
      return;
    }

    this.commentsPage.set(0);
    this.commentsLastPage.set(false);
    this.postComments.set([]);
    this.commentsLoading.set(true);

    this.fetchComments(false);
  }

  private fetchComments(append: boolean) {
    const postId = this.getPostId();
    if (!postId) return;

    const params: PageRequest = {
      page: this.commentsPage(),
      size: 10,
      sort: 'createdAt,asc',
    };

    this.postService.getPostComments(postId, params).subscribe({
      next: (res: PageResponse<Comment>) => {
        this.postComments.update((existing) => (append ? [...existing, ...res.data] : res.data));

        this.commentsLastPage.set(res.isLast);
        this.commentsLoading.set(false);
      },
      error: () => {
        this.commentsLoading.set(false);
        this.notify.showError('Failed to load comments');
      },
    });
  }

  private getPostId(): number | null {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = Number(idParam);
    return idParam && !isNaN(id) ? id : null;
  }
}
