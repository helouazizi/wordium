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
import { Report } from '../../../core/apis/posts/post.model';
import { PageRequest, PageResponse } from '../../models/pagination.model';
import { NotFound } from '../not-found/not-found';
import { MatIcon } from '@angular/material/icon';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { ReportCard } from '../report-card/report-card';

@Component({
  selector: 'app-report-list',
  imports: [NotFound,MatIcon,MatProgressSpinner,ReportCard],
  templateUrl: './report-list.html',
  styleUrl: './report-list.scss',
})
export class ReportList implements OnInit, AfterViewInit {
  @ViewChild('scrollAnchor') anchor!: ElementRef<HTMLElement>;
  private observer?: IntersectionObserver;

  private userService = inject(UsersService);
  private auth = inject(AuthService);
  private notify = inject(NotificationService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  emptyMessage = input<string>('No reports found here yet.');
  user = this.auth.user();

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

    this.userService.getAllReports(params).subscribe({
      next: (res: PageResponse<Report>) => {
        console.log(res.data, 'reports');

        this.reports.update((existing) =>
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
        this.notify.showError('Error loading reports');
      },
    });
  }

  resolveReport(id: number) {
    this.userService.resolve(id).subscribe({
      next: () => {
        this.reports.update((list) =>
          list.map((r) =>
            r.id === id ? { ...r, resolved: true, resolvedAt: new Date().toISOString() } : r,
          ),
        );

        this.notify.showSuccess('Report marked as resolved');
      },
      error: (err) => {
        console.error('Resolve report failed', err);
        this.notify.showError('Failed to resolve report');
      },
    });
  }

  deleteReport(id: number) {
    this.userService.deleteReport(id).subscribe({
      next: () => {
        this.reports.update((list) => list.filter((r) => r.id !== id));
        this.notify.showSuccess('Report deleted');
      },
      error: () => {
        this.notify.showError('Failed to delete report');
      },
    });
  }

  viewTarget(report: Report) {
    if (report.type === 'post') {
      this.router.navigate(['/posts/reports', report.reportedPostId]);
    } else {
      this.router.navigate(['/profiles/reports', report.reportedUserId]);
    }
  }
}
