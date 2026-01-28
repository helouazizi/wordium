import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { MatMenuModule } from '@angular/material/menu';
import { MatTabsModule } from '@angular/material/tabs';
import { MatDividerModule } from '@angular/material/divider';

interface Stat {
  label: string;
  value: string;
  icon: string;
  color: 'primary' | 'accent' | 'warn';
}

interface User {
  id: number;
  name: string;
  email: string;
  status: string;
  role: string;
  banned: boolean;
}

interface Post {
  id: number;
  title: string;
  author: string;
  snippet: string;
  status: 'Visible' | 'Hidden';
  reports: number;
}

interface Report {
  id: string;
  reason: string;
  user: string;
  post: string;
  priority: 'High' | 'Medium' | 'Low';
  status: 'Pending' | 'Resolved';
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
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard {
  selectedTabIndex = 0; // Users tab is default

  // ── Stats ───────────────────────────────────────────────
  stats: Stat[] = [
    { label: 'Total Users', value: '2,840', icon: 'people', color: 'primary' },
    { label: 'Total Posts', value: '1,452', icon: 'article', color: 'accent' },
    { label: 'Pending Reports', value: '18', icon: 'report_problem', color: 'warn' },
  ];

  // ── Users ───────────────────────────────────────────────
  users: User[] = [
    { id: 1, name: 'Alex Rivera', email: 'alex@example.com', status: 'Active', role: 'Admin', banned: false },
    { id: 2, name: 'Sarah Chen', email: 'sarah@example.com', status: 'Active', role: 'User', banned: false },
    { id: 3, name: 'Mike Johnson', email: 'mike@example.com', status: 'Banned', role: 'User', banned: true },
    { id: 4, name: 'Emily Davis', email: 'emily@example.com', status: 'Pending', role: 'User', banned: false },
  ];

  userColumns: (keyof User | 'actions')[] = ['name', 'email', 'status', 'role', 'actions'];

  // ── Posts ───────────────────────────────────────────────
  posts: Post[] = [
    { id: 101, title: 'Welcome & Rules', author: 'Alex Rivera', snippet: 'Community guidelines...', status: 'Visible', reports: 0 },
    { id: 102, title: '2026 Setup Guide', author: 'Sarah Chen', snippet: 'Best hardware in 2026...', status: 'Hidden', reports: 3 },
    { id: 103, title: 'UI/UX Trends', author: 'Mike Johnson', snippet: 'What’s hot right now...', status: 'Visible', reports: 1 },
  ];

  postColumns: (keyof Post | 'actions')[] = ['title', 'author', 'snippet', 'status', 'reports', 'actions'];

  // ── Reports ─────────────────────────────────────────────
  reports: Report[] = [
    { id: '#9921', reason: 'Spam / Promo', user: '@spamking22', post: 'Buy crypto cheap', priority: 'High', status: 'Pending' },
    { id: '#9923', reason: 'Harassment', user: '@angryuser99', post: 'You are useless', priority: 'High', status: 'Pending' },
    { id: '#9925', reason: 'Inappropriate media', user: '@anon_420', post: 'Offensive meme', priority: 'Medium', status: 'Pending' },
  ];

  reportColumns: (keyof Report | 'actions')[] = ['id', 'reason', 'user', 'post', 'priority', 'status', 'actions'];

  // ── Actions ─────────────────────────────────────────────
  banUser(user: User): void {
    user.banned = true;
    user.status = 'Banned';
  }

  unbanUser(user: User): void {
    user.banned = false;
    user.status = 'Active';
  }

  deleteUser(user: User): void {
    this.users = this.users.filter((u) => u.id !== user.id);
  }

  togglePostVisibility(post: Post): void {
    post.status = post.status === 'Visible' ? 'Hidden' : 'Visible';
  }

  deletePost(post: Post): void {
    this.posts = this.posts.filter((p) => p.id !== post.id);
  }

  resolveReport(report: Report): void {
    report.status = 'Resolved';
  }
}