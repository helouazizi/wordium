// dashboard-home.component.ts  (renamed class for clarity)
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { MatMenuModule } from '@angular/material/menu';
import { MatTabsModule } from '@angular/material/tabs';     // ← added
import { MatDividerModule } from '@angular/material/divider';

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
    MatDividerModule
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard {

  selectedTabIndex = 0; // 0 = Users (default)

  // ── Stats ───────────────────────────────────────────────
  stats = [
    { label: 'Total Users',   value: '2,840', icon: 'people',           color: 'primary' },
    { label: 'Total Posts',   value: '1,452', icon: 'article',          color: 'accent'  },
    { label: 'Pending Reports', value: '18',  icon: 'report_problem', color: 'warn'    }
  ];

  // ── Users ───────────────────────────────────────────────
  users = [
    { id: 1, name: 'Alex Rivera',   email: 'alex@example.com',   status: 'Active',  role: 'Admin',   banned: false },
    { id: 2, name: 'Sarah Chen',    email: 'sarah@example.com',  status: 'Active',  role: 'User',    banned: false },
    { id: 3, name: 'Mike Johnson',  email: 'mike@example.com',   status: 'Banned',  role: 'User',    banned: true  },
    { id: 4, name: 'Emily Davis',   email: 'emily@example.com',  status: 'Pending', role: 'User',    banned: false }
  ];
  userColumns = ['name', 'email', 'status', 'role', 'actions'];

  // ── Posts ───────────────────────────────────────────────
  posts = [
    { id: 101, title: 'Welcome & Rules',         author: 'Alex Rivera', snippet: 'Community guidelines...', status: 'Visible', reports: 0 },
    { id: 102, title: '2026 Setup Guide',        author: 'Sarah Chen',  snippet: 'Best hardware in 2026...', status: 'Hidden',  reports: 3 },
    { id: 103, title: 'UI/UX Trends',           author: 'Mike Johnson', snippet: 'What’s hot right now...', status: 'Visible', reports: 1 }
  ];
  postColumns = ['title', 'author', 'snippet', 'status', 'reports', 'actions'];

  // ── Reports ─────────────────────────────────────────────
  reports = [
    { id: '#9921', reason: 'Spam / Promo',       user: '@spamking22', post: 'Buy crypto cheap', priority: 'High',    status: 'Pending' },
    { id: '#9923', reason: 'Harassment',         user: '@angryuser99', post: 'You are useless',   priority: 'High',    status: 'Pending' },
    { id: '#9925', reason: 'Inappropriate media',user: '@anon_420',    post: 'Offensive meme',    priority: 'Medium', status: 'Pending' }
  ];
  reportColumns = ['id', 'reason', 'user', 'post', 'priority', 'status', 'actions'];

  // Actions
  banUser(user: any)   { user.banned = true;  user.status = 'Banned';  }
  unbanUser(user: any) { user.banned = false; user.status = 'Active';  }
  deleteUser(user: any) { this.users = this.users.filter(u => u.id !== user.id); }

  togglePostVisibility(post: any) {
    post.status = post.status === 'Visible' ? 'Hidden' : 'Visible';
  }
  deletePost(post: any) { this.posts = this.posts.filter(p => p.id !== post.id); }

  resolveReport(report: any) { report.status = 'Resolved'; }
}