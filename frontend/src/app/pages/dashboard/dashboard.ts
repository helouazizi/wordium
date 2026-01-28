// dashboard-home.component.ts
import { Component, TrackByFunction } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { MatMenuModule } from '@angular/material/menu';
import { MatTabsModule } from '@angular/material/tabs';
import { MatDividerModule } from '@angular/material/divider';
import { UserCard } from '../../shared/components/user-card/user-card';
import { ReportCard } from '../../shared/components/report-card/report-card';
import { UserList } from '../../shared/components/user-list/user-list';
import { PostList } from '../../shared/components/post-list/post-list';
import { ReportList } from '../../shared/components/report-list/report-list';

interface DashboardStat {
  id: string;
  label: string;
  value: string;
  icon: string;
  color: 'primary' | 'accent' | 'warn';
}




interface DashboardReport {
  id: string;
  reason: string;
  user: string;
  post: string;
  priority: 'High' | 'Medium' | 'Low';
  status: 'Pending' | 'Resolved';
  type : 'user' | 'post';
  reporter: string
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
    ReportList
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard {
  selectedTabIndex = 0;

  stats: DashboardStat[] = [
    { id: 'users', label: 'Total Users', value: '2,840', icon: 'people', color: 'primary' },
    { id: 'posts', label: 'Total Posts', value: '1,452', icon: 'article', color: 'accent' },
    { id: 'reports', label: 'Pending Reports', value: '18', icon: 'report_problem', color: 'warn' }
  ];


  protected readonly trackByStat: TrackByFunction<DashboardStat> = (_, stat) => stat.id;

  protected readonly trackByReport: TrackByFunction<DashboardReport> = (_, report) => report.id;



}