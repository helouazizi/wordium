import { booleanAttribute, Component, input, output } from '@angular/core';
import { Post, Reaction } from '../../../core/apis/posts/post.model';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { UserAvatar } from '../user-avatar/user-avatar';
import { MarkdownModule } from 'ngx-markdown';
import { CommonModule } from '@angular/common';
import { SafeHtmlPipe } from '../../pipes/safe-html.pipe';
@Component({
  selector: 'app-post-card',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    SafeHtmlPipe,
    UserAvatar,
    MarkdownModule,
  ],
  templateUrl: './post-card.html',
  styleUrl: './post-card.scss',
})
export class PostCard {
  post = input.required<Post>();

  open = output<number>();
  onReact = output<{ postId: number; reaction: Reaction }>();
  onComment = output<string>();
  onReport = output<string>();
  onDelete = output<void>();
  onMark = output<void>();
  showReadMoreOverlay = input<boolean, boolean | undefined>(true, { transform: booleanAttribute });

  navigateToPost() {
    this.open.emit(this.post().id);
    // Prevent navigation when clicking buttons/menus inside card
    // if ((event.target as HTMLElement).closest('button, a[mat-menu-item]')) {
    //   return;
    // }
  }

  handleLike() {
    this.onReact.emit({
      postId: this.post().id,
      reaction: 'like',
    });
  }

  handleComment() {
    this.onComment.emit('');
  }

  handleReport() {
    this.onReport.emit('this.post().id');
  }

  handleDelete() {
    this.onDelete.emit();
  }

  handleMarke() {
    this.onMark.emit();
  }
}
