import { Component, Input, Output, EventEmitter, inject } from '@angular/core';
import { Post } from '../../../core/apis/posts/modles';
import { UserProfile } from '../user-profile/user-profile';
import { CommonModule } from '@angular/common';
import { SafeHtmlPipe } from '../../pipes/safe-html.pipe';
import { FormsModule } from '@angular/forms';
import { User } from '../../models/user';

import { Reaction } from '../../../core/apis/posts/modles';
import { FeedFacade } from '../../../features/posts/feed/feed.facade';

@Component({
  selector: 'app-post-card',
  standalone: true,
  imports: [CommonModule, FormsModule, UserProfile, SafeHtmlPipe],
  templateUrl: './post-card.html',
  styleUrl: './post-card.scss',
})
export class PostCard {
  private facade = inject(FeedFacade);
  @Input({ required: true }) post!: Post;
  @Input({ required: true }) user: User | null = null;
  @Input() showContent = false;
  @Input() mode: 'feed' | 'detail' = 'feed';

  @Output() open = new EventEmitter<number>();

  isMenuOpen = false;
  commentText = '';

  get isOwner(): boolean {
    return this.user ? this.post.actor.id === this.user.id : false;
  }

  get isAdmin(): boolean {
    return this.user ? this.user.role === 'ADMIN' : false;
  }

  toggleMenu(event: Event) {
    event.stopPropagation();
    this.isMenuOpen = !this.isMenuOpen;
  }

  openPost() {
    this.open.emit(this.post.id);
  }
  reactPost() {
    if (!this.user) return;
    const reaction: Reaction = this.post.isLiked ? 'unlike' : 'like';
    this.facade.reactPost(this.post.id, reaction);
  }

  sendComment() {
    if (!this.user) return;
    if (!this.commentText.trim()) return;
    this.facade.addComment(this.post.id, this.commentText);
    this.commentText = '';
  }

  reportPost() {
    if (!this.user) return;
    this.facade.reportPost(this.post.id, 'Inappropriate content');
    this.isMenuOpen = false;
  }

  deletePost() {
    if (!this.user) return;
    if (confirm('Are you sure you want to delete this post?')) {
      this.facade.deletePost(this.post.id);
    }
    this.isMenuOpen = false;
  }
}
