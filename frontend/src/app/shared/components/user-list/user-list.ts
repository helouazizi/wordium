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
export type UserListSource = 'dashboard' | 'search';
@Component({
  selector: 'app-user-list',
  imports: [UserCard,MatProgressSpinner, MatSpinner, MatIcon, NotFound],
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


    this.userService.getAllUsers(params).subscribe({
      next: (res: PageResponse<User>) => {
        console.log(res.data,"users");
        
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
}
