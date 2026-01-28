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

// ===== TYPE DEFINITIONS (Critical for maintainability) =====
interface DashboardStat {
  id: string;
  label: string;
  value: string;
  icon: string;
  color: 'primary' | 'accent' | 'warn';
}

interface DashboardUser {
  id: number;
  username: string;
  email: string;
  status: 'Active' | 'Banned' | 'Pending';
  role: 'Admin' | 'User';
  banned: boolean;
}

interface DashboardPost {
  id: number;
  title: string;
  author: string;
  snippet: string;
  status: 'Visible' | 'Hidden';
  reports: number;
}

interface DashboardReport {
  id: string;
  reason: string;
  user: string;
  post: string;
  priority: 'High' | 'Medium' | 'Low';
  status: 'Pending' | 'Resolved';
  type : 'user' | 'post';
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
    UserCard,
    ReportCard
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard {
  selectedTabIndex = 0;

  // ===== TYPED DATA STRUCTURES =====
  stats: DashboardStat[] = [
    { id: 'users', label: 'Total Users', value: '2,840', icon: 'people', color: 'primary' },
    { id: 'posts', label: 'Total Posts', value: '1,452', icon: 'article', color: 'accent' },
    { id: 'reports', label: 'Pending Reports', value: '18', icon: 'report_problem', color: 'warn' }
  ];

  users: DashboardUser[] = [
    { id: 1, username: 'Alex Rivera', email: 'alex@example.com', status: 'Active', role: 'Admin', banned: false },
    { id: 2, username: 'Sarah Chen', email: 'sarah@example.com', status: 'Active', role: 'User', banned: false },
    { id: 3, username: 'Mike Johnson', email: 'mike@example.com', status: 'Banned', role: 'User', banned: true },
    { id: 4, username: 'Emily Davis', email: 'emily@example.com', status: 'Pending', role: 'User', banned: false }
  ];
  userColumns = ['name', 'email', 'status', 'role', 'actions'] as const;

  posts: DashboardPost[] = [
    { id: 101, title: 'Welcome & Rules', author: 'Alex Rivera', snippet: 'Community guidelines...', status: 'Visible', reports: 0 },
    { id: 102, title: '2026 Setup Guide', author: 'Sarah Chen', snippet: 'Best hardware in 2026...', status: 'Hidden', reports: 3 },
    { id: 103, title: 'UI/UX Trends', author: 'Mike Johnson', snippet: 'What\'s hot right now...', status: 'Visible', reports: 1 }
  ];
  postColumns = ['title', 'author', 'snippet', 'status', 'reports', 'actions'] as const;

  reports: DashboardReport[] = [
    { id: '#9921', reason: 'Spam / Promo', user: '@spamking22', post: 'Buy crypto cheap', priority: 'High', status: 'Pending', type: 'user' },
    { id: '#9923', reason: 'Harassment', user: '@angryuser99', post: 'You are useless', priority: 'High', status: 'Pending' , type: 'post'},
    { id: '#9925', reason: 'Inappropriate media', user: '@anon_420', post: 'Offensive meme', priority: 'Medium', status: 'Pending', type: 'user' }
  ];
  reportColumns = ['id', 'reason', 'user', 'post', 'priority', 'status', 'actions'] as const;

  // ===== TRACKBY FUNCTIONS (Critical for performance) =====
  protected readonly trackByStat: TrackByFunction<DashboardStat> = (_, stat) => stat.id;
  protected readonly trackByUser: TrackByFunction<DashboardUser> = (_, user) => user.id;
  protected readonly trackByPost: TrackByFunction<DashboardPost> = (_, post) => post.id;
  protected readonly trackByReport: TrackByFunction<DashboardReport> = (_, report) => report.id;

  // ===== TYPED ACTION HANDLERS =====
  banUser(user: DashboardUser): void {
    user.banned = true;
    user.status = 'Banned';
  }

  unbanUser(user: DashboardUser): void {
    user.banned = false;
    user.status = 'Active';
  }

  deleteUser(user: DashboardUser): void {
    this.users = this.users.filter(u => u.id !== user.id);
  }

  togglePostVisibility(post: DashboardPost): void {
    post.status = post.status === 'Visible' ? 'Hidden' : 'Visible';
  }

  deletePost(post: DashboardPost): void {
    this.posts = this.posts.filter(p => p.id !== post.id);
  }

  resolveReport(report: DashboardReport): void {
    report.status = 'Resolved';
  }
}