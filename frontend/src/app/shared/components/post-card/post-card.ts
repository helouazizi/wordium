import {
  Component,
  input,
  output,
  signal,
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
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialog } from '../confirm-dialog/confirm-dialog';

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
  private dialog = inject(MatDialog);
  private router = inject(Router);
  post = input.required<Post>();
  user = input.required<User>();
  mode = input<PostListSource>('feed');

  onReact = output<number>();
  onUpdate = output<Post>();
  onBookmark = output<number>();
  onDelete = output<number>();
  onVisible = output<number>();
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

  newCommentText = signal('');

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

  readonly isOwn = computed(() => {
    const user = this.user();
    const post = this.post();
    if (!user || !post) return false;
    return String(post.actor.id) === String(user.id);
  });

  handleReact(id: number) {
    this.onReact.emit(id);
  }

  handleUpdate() {
    this.onUpdate.emit(this.post());
  }

  submitComment() {
    if (this.newCommentText().trim()) {
      this.onComment.emit(this.newCommentText());
      this.newCommentText.set('');
    }
  }

  handleNavigate() {
    if (this.mode() !== 'detail') this.router.navigate(['/posts/', this.post().id]);
  }

  loadMoreComments() {
    this.onLoadMoreComments.emit();
  }

  confirmVisibility() {
    this.dialog
      .open(ConfirmDialog, {
        width: '420px',
        disableClose: true,
        data: {
          title: 'Hide Post',
          message: 'This post will be permanently hiden.',
          confirmText: 'Hide',
          color: 'warn',
        },
      })
      .afterClosed()
      .subscribe((res) => {
        if (res?.confirmed) {
          this.onVisible.emit(this.post().id);
        }
      });
  }

  confirmDeletePost() {
    this.dialog
      .open(ConfirmDialog, {
        width: '420px',
        disableClose: true,
        data: {
          title: 'Delete Post',
          message: 'This post will be permanently deleted.',
          confirmText: 'Delete',
          color: 'warn',
        },
      })
      .afterClosed()
      .subscribe((res) => {
        if (res?.confirmed) {
          this.onDelete.emit(this.post().id);
        }
      });
  }

  confirmDeleteComment(commentId: number) {
    this.dialog
      .open(ConfirmDialog, {
        width: '420px',
        disableClose: true,
        data: {
          title: 'Delete Comment',
          message: 'This comment will be permanently deleted.',
          confirmText: 'Delete',
          color: 'warn',
        },
      })
      .afterClosed()
      .subscribe((res) => {
        if (res?.confirmed) {
          this.onDeleteComment.emit(commentId);
        }
      });
  }

  confirmReportPost() {
    this.dialog
      .open(ConfirmDialog, {
        width: '450px',
        disableClose: true,
        data: {
          title: 'Report Post',
          message: 'Please provide a reason for reporting this post.',
          confirmText: 'Report',
          requireReason: true,
          reasonLabel: 'Report reason',
          minReasonLength: 10,
          color: 'warn',
        },
      })
      .afterClosed()
      .subscribe((res) => {
        if (res?.confirmed) {
          const reason = res.reason;
          this.dialog
            .open(ConfirmDialog, {
              width: '400px',
              disableClose: true,
              data: {
                title: 'Confirm Report',
                message: 'Are you sure you want to report this post?',
                confirmText: 'Yes, Report',
                cancelText: 'Cancel',
                color: 'warn',
              },
            })
            .afterClosed()
            .subscribe((confirmed) => {
              if (confirmed) {
                this.onReport.emit({
                  id: this.post().id,
                  type: 'post',
                  reason,
                });
              }
            });
        }
      });
  }

  confirmReportAuthor() {
    this.dialog
      .open(ConfirmDialog, {
        width: '450px',
        disableClose: true,
        data: {
          title: 'Report User',
          message: 'Please provide a reason for reporting this user.',
          confirmText: 'Report User',
          requireReason: true,
          reasonLabel: 'Report reason',
          minReasonLength: 10,
          color: 'warn',
        },
      })
      .afterClosed()
      .subscribe((res) => {
        if (res?.confirmed) {
          const reason = res.reason;
   
          this.dialog
            .open(ConfirmDialog, {
              width: '400px',
              disableClose: true,
              data: {
                title: 'Confirm Report',
                message: 'Are you sure you want to report this post author?',
                confirmText: 'Yes, Report',
                cancelText: 'Cancel',
                color: 'warn',
              },
            })
            .afterClosed()
            .subscribe((confirmed) => {
              if (confirmed) {
                this.onReport.emit({
                  id: this.post().id,
                  type: 'user',
                  reason,
                });
              }
            });
        }
      });
  }
}
