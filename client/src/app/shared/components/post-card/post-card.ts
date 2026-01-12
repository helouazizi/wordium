import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Post } from '../../../core/apis/posts/modles';
import { UserProfile } from '../user-profile/user-profile';
import { CommonModule } from '@angular/common';
import { SafeHtmlPipe } from '../../pipes/safe-html.pipe';

@Component({
  selector: 'app-post-card',
  standalone: true,
  imports: [CommonModule, UserProfile , SafeHtmlPipe],
  templateUrl: './post-card.html',
  styleUrls: ['./post-card.scss'],
})
export class PostCard {
  @Input({ required: true }) post!: Post;
  @Input() showContent!: boolean;
  @Output() open = new EventEmitter<number>();

  openPost() {
    this.open.emit(this.post.id);
  }
}
