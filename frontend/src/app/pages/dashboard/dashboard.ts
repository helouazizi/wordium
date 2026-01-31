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
      usersReports: this.usersService.getUsersReportsCount(),
      posts: this.usersService.getPostCount(),
      postsReports: this.usersService.getPostReporstCount(),
    }).subscribe({
      next: ({ users, usersReports, posts, postsReports }) => {
        console.log(postsReports, 'posts reports ');

        this.stats.set([
          {
            id: 'users',
            label: 'Total Users',
            value: users.total.toLocaleString(),
            icon: 'people',
            color: 'primary',
          },
          {
            id: 'usersReports',
            label: 'Total Users Reports',
            value: usersReports.total.toLocaleString(),
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
          {
            id: 'postsReports',
            label: 'Total Posts Reports',
            value: postsReports.total.toLocaleString(),
            icon: 'report_problem',
            color: 'warn',
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

  private decrementStat(id: DashboardStat['id'], by = 1) {
    this.stats.update((stats) =>
      stats.map((stat) =>
        stat.id === id
          ? {
              ...stat,
              value: Math.max(0, Number(stat.value.replace(/,/g, '')) - by).toLocaleString(),
            }
          : stat,
      ),
    );
  }
  decrementUsers(by = 1) {
    this.decrementStat('users', by);
  }

  decrementPosts(by = 1) {
    this.decrementStat('posts', by);
  }

  decrementReports(type: 'user' | 'post', by = 1) {
    if (type === 'user') {
      this.decrementStat('usersReports', by);
    } else {
      this.decrementStat('postsReports', by);
    }
  }

  protected readonly trackByStat: TrackByFunction<DashboardStat> = (_, stat) => stat.id;
}
