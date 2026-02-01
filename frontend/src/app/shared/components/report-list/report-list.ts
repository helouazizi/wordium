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
import { Report } from '../../../core/apis/posts/post.model';
import { PageRequest, PageResponse } from '../../models/pagination.model';
import { NotFound } from '../not-found/not-found';
import { MatIcon } from '@angular/material/icon';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { ReportCard } from '../report-card/report-card';
import { forkJoin } from 'rxjs';
import { DeviceService } from '../../../core/services/device.service';

@Component({
  selector: 'app-report-list',
  imports: [NotFound, MatIcon, MatProgressSpinner, ReportCard],
  templateUrl: './report-list.html',
  styleUrl: './report-list.scss',
})
export class ReportList implements OnInit, AfterViewInit {
  @ViewChild('scrollAnchor') anchor!: ElementRef<HTMLElement>;
  private observer?: IntersectionObserver;

  private userService = inject(UsersService);

  private device = inject(DeviceService);
  private auth = inject(AuthService);
  private notify = inject(NotificationService);
  private router = inject(Router);

  @Output() ondeleted = new EventEmitter<'user' | 'post'>();

  emptyMessage = input<string>('No reports found here yet.');
  user = this.auth.user();
  isHandset = this.device.isHandset;
  currentPage = signal(0);
  isLastPage = signal(false);

  reports = signal<Report[]>([]);
  initialLoading = signal(false);
  pageLoading = signal(false);

  err = signal<string | null>(null);
  isEmpty = () => !this.initialLoading() && this.reports().length === 0;
  endReached = () => this.isLastPage() && !this.initialLoading() && this.reports().length > 0;

  ngOnInit() {
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
    this.reports.set([]);
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

    forkJoin({
      posts: this.userService.getPostReports(params),
      users: this.userService.getUserReports(params),
    }).subscribe({
      next: ({ posts, users }) => {
        const merged = [...posts.data, ...users.data].sort(
          (a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime(),
        );

        this.reports.update((existing) =>
          append
            ? Array.from(
                new Map([...existing, ...merged].map((p) => [`${p.type}-${p.id}`, p])).values(),
              )
            : merged,
        );


        this.isLastPage.set(posts.isLast && users.isLast);

        this.initialLoading.set(false);
        this.pageLoading.set(false);
      },
      error: () => this.handleError(),
    });
  }

  viewTarget(report: Report) {
    if (report.type === 'user' && report.reportedUserId != null) {
      this.router.navigate(['/profiles', report.reportedUserId]);
    } else {
      this.router.navigate(['/posts', report.reportedPostId]);
    }
  }

  private handleError() {
    this.initialLoading.set(false);
    this.pageLoading.set(false);
    this.notify.showError('Error loading reports');
  }

  resolveReport(report: Report) {
    const call =
      report.type === 'user'
        ? this.userService.resolveUserReport(report.id)
        : this.userService.resolvePostReport(report.id);

    call.subscribe({
      next: () => {
        this.reports.update((list) =>
          list.map((r) =>
            r.id === report.id && r.type === report.type
              ? { ...r, resolved: true, resolvedAt: new Date().toISOString() }
              : r,
          ),
        );
        this.notify.showSuccess('Report resolved');
      },
      error: () => this.notify.showError('Failed to resolve report'),
    });
  }

  deleteReport(report: Report) {
    const call =
      report.type === 'user'
        ? this.userService.deleteUserReport(report.id)
        : this.userService.deletePostReport(report.id);

    call.subscribe({
      next: () => {
        this.reports.update((list) =>
          list.filter((r) => r.id !== report.id || r.type !== report.type),
        );
        this.ondeleted.emit(report.type)
        this.notify.showSuccess('Report deleted');
      },
      error: () => this.notify.showError('Failed to delete report'),
    });
  }
}
