import {
  Component,
  input,
  output,
  signal,
  effect,
  computed,
  inject,
  EventEmitter,
} from '@angular/core';
import { Post, Comment, ReportType } from '../../../core/apis/posts/post.model';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { FormsModule } from '@angular/forms';
import { UserAvatar } from '../user-avatar/user-avatar';
import { MarkdownModule } from 'ngx-markdown';
import { CommonModule } from '@angular/common';
import { SafeHtmlPipe } from '../../pipes/safe-html.pipe';
import { User } from '../../models/user.model';
import { PageRequest } from '../../models/pagination.model';
import { Router } from '@angular/router';
import { PostListSource } from '../post-list/post-list';

@Component({
  selector: 'app-post-card',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatDividerModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressSpinnerModule,
    SafeHtmlPipe,
    UserAvatar,
    MarkdownModule,
  ],
  templateUrl: './post-card.html',
  styleUrl: './post-card.scss',
})
export class PostCard {
  private router = inject(Router);
  post = input.required<Post>();
  user = input.required<User>();
  mode = input<PostListSource>('feed');

  onReact = output<number>();
  onBookmark = output<number>();
  onDelete = output<number>();
  onReport = output<{ id: number; type: ReportType; reason: string }>();
  onComment = output<string>();
  private size = 10;
  onRequestComments = output<{ postId: number; page: PageRequest }>();

  comments = signal<Comment[]>([]);
  currentPage = signal(0);
  isLoadingComments = signal(false);
  hasMoreComments = signal(true);

  isConfirmingDelete = signal(false);
  newCommentText = signal('');
  isReporting = signal(false);
  reportReason = signal('');
  reportingTarget = signal<{ id: number; type: ReportType } | null>(null);

  reportOptions = computed(() => [
    { label: 'The Post', id: this.post().id, type: 'post' as ReportType },
    { label: 'The Author', id: this.post().actor.id as number, type: 'user' as ReportType },
  ]);

  confirmDelete() {
    this.isConfirmingDelete.set(true);
  }
  cancelDelete() {
    this.isConfirmingDelete.set(false);
  }
  readonly canDelete = computed(() => {
    const user = this.user();
    const post = this.post();
    if (!user || !post) return false;
    return String(post.actor.id) === String(user.id) || user.role === 'ADMIN';
  });

  readonly isAdmin = computed(() => {
    const user = this.user();
    const post = this.post();
    if (!user || !post) return false;
    return user.role === 'ADMIN';
  });
  constructor() {
    effect(() => {
      if (this.mode() === 'detail' && this.comments().length === 0) {
        this.loadMore();
      }
    });
  }

  loadMore() {
    if (this.isLoadingComments() || !this.hasMoreComments()) return;
    this.isLoadingComments.set(true);
    const page: PageRequest = {
      page: this.currentPage(),
      size: this.size,
      sort: 'createdAt,desc',
    };
    this.onRequestComments.emit({ postId: this.post().id, page: page });
  }

  updateComments(newComments: Comment[], isLastPage: boolean) {
    this.comments.update((prev) => [...prev, ...newComments]);
    this.hasMoreComments.set(!isLastPage);
    this.currentPage.update((p) => p + 1);
    this.isLoadingComments.set(false);
  }

  openReport(id: number, type: ReportType) {
    this.reportingTarget.set({ id, type });
    this.isReporting.set(true);
  }

  handleReact(id: number) {
    this.onReact.emit(id);
    
  }
  selectReportTarget(id: number, type: ReportType) {
    this.reportingTarget.set({ id, type });
  }

  submitReport() {
    const target = this.reportingTarget();
    if (target && this.reportReason().trim()) {
      this.onReport.emit({
        id: target.id,
        type: target.type,
        reason: this.reportReason(),
      });
      this.cancelReport();
    }
  }

  cancelReport() {
    this.isReporting.set(false);
    this.reportReason.set('');
    this.reportingTarget.set(null);
  }

  submitComment() {
    if (this.newCommentText().trim()) {
      this.onComment.emit(this.newCommentText());
      this.newCommentText.set('');
    }
  }

  executeDelete() {
    this.onDelete.emit(this.post().id);
    this.isConfirmingDelete.set(false);
  }
  handleNavigate() {
    if (this.mode() === 'feed') this.router.navigate(['/posts/', this.post().id]);
  }
}
