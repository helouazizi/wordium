import { Component, inject, input, output, computed, signal } from '@angular/core';
import { Post } from '../../../core/apis/posts/modles';
import { UserProfile } from '../user-profile/user-profile';
import { CommonModule } from '@angular/common';
import { SafeHtmlPipe } from '../../pipes/safe-html.pipe';
import { FormsModule } from '@angular/forms';
import { User } from '../../models/user';
import { SessionService } from '../../../core/services/session.service';

@Component({
  selector: 'app-post-card',
  standalone: true,
  imports: [CommonModule, FormsModule, UserProfile, SafeHtmlPipe],
  templateUrl: './post-card.html',
  styleUrl: './post-card.scss',
})
export class PostCard {
  private session = inject(SessionService);

  post = input.required<Post>();
  readonly currentUser = this.session.user;
  showContent = input(false);
  mode = input<'feed' | 'detail'>('feed');

  open = output<number>();
  like = output<void>();
  comment = output<string>();
  delete = output<void>();
  report = output<string>();

  isMenuOpen = signal(false);
  commentText = signal('');
  reportText = signal('');

  readonly canDelete = computed(() => {
    const user = this.currentUser();
    const post = this.post();

    if (!user || !post) return false;

    const isOwner = String(post.actor.id) === String(user.id);
    const isAdmin = user.role === 'ADMIN';

    return isOwner || isAdmin;
  });

  toggleMenu(event: Event) {
    event.stopPropagation();
    this.isMenuOpen.update((v) => !v);
  }

  onLikeClick(event: Event) {
    event.stopPropagation();
    this.like.emit();
  }

  submitComment() {
    const text = this.commentText().trim();
    if (text) {
      this.comment.emit(text);
      this.commentText.set('');
    }
  }

  onDeleteClick() {
    if (confirm('Are you sure you want to delete this post?')) {
      this.delete.emit();
    }
  }

  openPost() {
    this.open.emit(this.post().id);
  }

  onReportClick() {
    const text = this.reportText().trim();
    if (text) {
      this.report.emit(text);
      this.reportText.set('');
    }
  }
}
