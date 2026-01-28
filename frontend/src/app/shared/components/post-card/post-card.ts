import {
  Component,
  input,
  output,
  signal,
  effect,
  computed,
  inject,
  EventEmitter,
  Output,
  Input,
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

  @Output() onLoadMoreComments = new EventEmitter<void>();
  @Output() onDeleteComment = new EventEmitter<number>();

  @Input() comments: Comment[] = [];
  @Input() commentsLoading = false;
  @Input() commentsLastPage = false;

  currentPage = signal(0);
  isLoadingComments = signal(false);
  hasMoreComments = signal(true);

  isConfirmingDelete = signal(false);
  isConfirmingCommentDelete = signal(false);
  commentId = signal<number | null>(null);
  newCommentText = signal('');
  isReporting = signal(false);
  isConfirmingReport = signal(false);
  reportReason = signal('');
  reportingTarget = signal<{ id: number; type: ReportType } | null>(null);

  reportOptions = computed(() => [
    { label: 'The Post', id: this.post().id, type: 'post' as ReportType },
    { label: 'The Author', id: this.post().actor.id as number, type: 'user' as ReportType },
  ]);

  confirmDelete() {
    this.isConfirmingDelete.set(true);
  }

  confirmCommentDelete(id: number) {
    this.isConfirmingCommentDelete.set(true);
    this.commentId.set(id);
  }
  confirmReport() {
    this.isConfirmingReport.set(true);
    this.isReporting.set(false);
  }
  cancelDelete() {
    this.isConfirmingDelete.set(false);
  }
  cancelCommentDelete() {
    this.isConfirmingCommentDelete.set(false);
    this.commentId.set(null);
  }
  DeleteComment() {
    this.onDeleteComment.emit(this.commentId()!);
    this.isConfirmingCommentDelete.set(false)
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
    this.isConfirmingReport.set(false);
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
    if (this.mode() !== 'detail') this.router.navigate(['/posts/', this.post().id]);
  }

  loadMoreComments() {
    this.onLoadMoreComments.emit();
  }
}
