import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Post } from '../../../core/apis/posts/modles';
import { UserProfile } from '../user-profile/user-profile';
import { CommonModule } from '@angular/common';
import { SafeHtmlPipe } from '../../pipes/safe-html.pipe';
import { FormsModule, NgModel, NgModelGroup } from '@angular/forms';
import { User } from '../../models/user';

@Component({
  selector: 'app-post-card',
  standalone: true,
  imports: [CommonModule, FormsModule, UserProfile, SafeHtmlPipe],
  templateUrl: './post-card.html',
  styleUrls: ['./post-card.scss'],
})
export class PostCard {
  @Input({ required: true }) post!: Post;
  @Input({ required: true }) user: User | null = null;
  @Input() showContent!: boolean;
  @Input() mode: 'feed' | 'detail' = 'feed';

  @Output() open = new EventEmitter<number>();
  @Output() onComment = new EventEmitter<string>();
  @Output() onDelete = new EventEmitter<number>();
  @Output() onReport = new EventEmitter<number>();
  @Output() onLike = new EventEmitter<number>();

  openPost() {
    this.open.emit(this.post.id);
  }

  isMenuOpen = false;
  commentText = '';

  get isOwner(): boolean {
    if (this.user == null) {
      return false;
    }
    return this.post.actor.id === this.user.id;
  }

  toggleMenu(event: Event) {
    event.stopPropagation();
    this.isMenuOpen = !this.isMenuOpen;
  }

  sendComment() {
    if (!this.commentText.trim()) return;
    this.onComment.emit(this.commentText);
    this.commentText = '';
  }

  reportPost() {
    this.onReport.emit(this.post.id);
    this.isMenuOpen = false;
  }

  reacttPost() {
    this.onLike.emit(this.post.id);
  }

  deletePost() {
    if (confirm('Are you sure you want to delete this post?')) {
      this.onDelete.emit(this.post.id);
    }
    this.isMenuOpen = false;
  }
}
