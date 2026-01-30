import { Component, inject, OnInit, signal, TrackByFunction } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { MatMenuModule } from '@angular/material/menu';
import { MatTabsModule } from '@angular/material/tabs';
import { MatDividerModule } from '@angular/material/divider';
import { forkJoin } from 'rxjs';

import { UserList } from '../../shared/components/user-list/user-list';
import { PostList } from '../../shared/components/post-list/post-list';
import { ReportList } from '../../shared/components/report-list/report-list';
import { UsersService } from '../../core/services/users.service';

interface DashboardStat {
  id: string;
  label: string;
  value: string;
  icon: string;
  color: 'primary' | 'accent' | 'warn';
}

@Component({
  selector: 'app-dashboard-home',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    MatTableModule,
    MatChipsModule,
    MatMenuModule,
    MatTabsModule,
    MatDividerModule,
    UserList,
    PostList,
    ReportList,
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard implements OnInit {
  private readonly usersService = inject(UsersService);

  readonly loading = signal(false);
  readonly stats = signal<DashboardStat[]>([]);
  selectedTabIndex = 0;

  ngOnInit(): void {
    this.loadDashboardStats();
  }

  private loadDashboardStats(): void {
    this.loading.set(true);

    forkJoin({
      users: this.usersService.getUsersCount(),
      reports: this.usersService.getUsersReportsCount(),
      posts: this.usersService.getPostCount(),
    }).subscribe({
      next: ({ users, reports,posts }) => {
        console.log(users, reports);

        this.stats.set([
          {
            id: 'users',
            label: 'Total Users',
            value: users.total.toLocaleString(),
            icon: 'people',
            color: 'primary',
          },
          {
            id: 'reports',
            label: 'Total Users Reports',
            value: reports.total.toLocaleString(),
            icon: 'report_problem',
            color: 'warn',
          },
          {
            id: 'posts',
            label: 'Total Posts',
            value: posts.total.toLocaleString(),
            icon: 'article',
            color: 'accent',
          },
        ]);

        this.loading.set(false);
      },
      error: (err) => {
        console.error(err);
        this.loading.set(false);
      },
    });
  }

  protected readonly trackByStat: TrackByFunction<DashboardStat> = (_, stat) => stat.id;
}
