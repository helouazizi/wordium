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
import { UsersService } from '../../../core/services/users.service';
import { AuthService } from '../../../core/services/auth.service';
import { NotificationService } from '../../../core/services/notification.service';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../../models/user.model';
import { PageRequest, PageResponse } from '../../models/pagination.model';
import { UserCard } from '../user-card/user-card';
import { MatProgressSpinner, MatSpinner } from '@angular/material/progress-spinner';
import { MatIcon } from '@angular/material/icon';
import { NotFound } from '../not-found/not-found';
import { distinctUntilChanged, filter, map, Observable, switchMap } from 'rxjs';
export type UserListSource = 'dashboard' | 'search' | 'followers' | 'following';
@Component({
  selector: 'app-user-list',
  imports: [UserCard, MatProgressSpinner, MatSpinner, MatIcon, NotFound],
  templateUrl: './user-list.html',
  styleUrl: './user-list.scss',
})
export class UserList implements OnInit, AfterViewInit {
  @ViewChild('scrollAnchor') anchor!: ElementRef<HTMLElement>;
  private observer?: IntersectionObserver;

  private userService = inject(UsersService);
  private auth = inject(AuthService);
  private notify = inject(NotificationService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  source = input.required<UserListSource>();

  emptyMessage = input<string>('No users found here yet.');
  user = this.auth.user();
  @Output() userDeleted = new EventEmitter<void>();

  currentPage = signal(0);
  isLastPage = signal(false);

  users = signal<User[]>([]);
  initialLoading = signal(false);
  pageLoading = signal(false);

  err = signal<string | null>(null);
  isEmpty = () => !this.initialLoading() && this.users().length === 0;
  endReached = () => this.isLastPage() && !this.initialLoading() && this.users().length > 0;

  ngOnInit() {
    if (!this.source()) {
      throw new Error('UserList: source input is required');
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
    this.users.set([]);
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
    let result$ = this.resolveApiCall(params);

    result$.subscribe({
      next: (res: PageResponse<User>) => {

        this.users.update((existing) =>
          append
            ? Array.from(new Map([...existing, ...res.data].map((p) => [p.id, p])).values())
            : res.data,
        );

        this.isLastPage.set(res.isLast);
        this.initialLoading.set(false);
        this.pageLoading.set(false);
      },
      error: () => {
        this.initialLoading.set(false);
        this.pageLoading.set(false);
        this.notify.showError('Error loading users');
      },
    });
  }

  private resolveApiCall(params: PageRequest): Observable<PageResponse<User>> {
    switch (this.source()) {
      case 'following':
        return this.userId$.pipe(
          filter((id): id is number => id !== null),
          switchMap((id) => this.userService.getUserFollowing(id, params)),
        );

      case 'followers':
        return this.userId$.pipe(
          filter((id): id is number => id !== null),
          switchMap((id) => this.userService.getUserFollowers(id, params)),
        );

      default:
        return this.userService.getAllUsers(params);
    }
  }

  deleteUser(id: number) {
    this.userService.deleteUser(id).subscribe({
      next: () => {
        this.users.update((list) => list.filter((u) => u.id !== id));
        this.notify.showSuccess(`The user has been deleted.`);
        this.userDeleted.emit();
      },
      error: () => {
        this.notify.showError(`Failed to delete this user`);
      },
    });
  }

  banUser(id: number) {
    this.userService.banUser(id).subscribe({
      next: () => {
        this.users.update((list) => list.map((u) => (u.id === id ? { ...u, isBanned: true } : u)));
        this.notify.showSuccess(`The user has been banned`);
      },
      error: () => {
        this.notify.showError(`Failed to ban this user`);
      },
    });
  }

  unbanUser(id: number) {
    this.userService.unbanUser(id).subscribe({
      next: () => {
        this.users.update((list) => list.map((u) => (u.id === id ? { ...u, isBanned: false } : u)));
        this.notify.showSuccess(`The user has been unbanned.`);
      },
      error: () => {
        this.notify.showError(`Failed to unban this user`);
      },
    });
  }

  viewProfile(id: number) {
    this.router.navigate(['profiles/', id]);
  }
  togleFollow(id: number) {
    const target = this.users().find((u) => u.id === id);
    if (!target || !this.user || this.user.id === id) return;

    const wasFollowing = target.isFollowing ?? false;

    const currentStats = target.stats ?? {
      followers: 0,
      following: 0,
      posts: 0,
      bookmarks: 0,
    };

    const action$ = wasFollowing
      ? this.userService.unfollowUser(id)
      : this.userService.followUser(id);

    this.users.update((list) =>
      list.map((u) => {
        if (u.id === id) {
          return {
            ...u,
            isFollowing: !wasFollowing,
            stats: {
              followers: wasFollowing
                ? Math.max(0, (u.stats?.followers ?? 0) - 1)
                : (u.stats?.followers ?? 0) + 1,
              following: u.stats?.following ?? 0,
              posts: u.stats?.posts ?? 0,
              bookmarks: u.stats?.bookmarks ?? 0,
            },
          } as User;
        }
        return u;
      }),
    );

    action$.subscribe({
      next: () => {
        this.notify.showSuccess(
          !wasFollowing
            ? `You are now following ${target.username}`
            : `Unfollowed ${target.username}`,
        );
      },
      error: (err) => {
        console.error('Follow/unfollow failed', err);

        this.users.update((list) => list.map((u) => (u.id === id ? target : u)));
        this.notify.showError(
          `Failed to ${wasFollowing ? 'unfollow' : 'follow'} ${target.username}`,
        );
      },
    });
  }

  private userId$ = this.route.paramMap.pipe(
    map((pm) => {
      const idParam = pm.get('id');
      const id = Number(idParam);
      return idParam && !isNaN(id) ? id : null;
    }),
    distinctUntilChanged(),
  );
}
