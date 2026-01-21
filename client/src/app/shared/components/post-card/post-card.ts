import {
  Component,
  inject,
  input,
  output,
  computed,
  signal,
  effect,
  untracked,
} from '@angular/core';
import { Comment, Post } from '../../../core/apis/posts/modles';
import { UserProfile } from '../user-profile/user-profile';
import { CommonModule } from '@angular/common';
import { SafeHtmlPipe } from '../../pipes/safe-html.pipe';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../../core/services/session.service';
import { PostsService } from '../../../core/services/posts.service';

@Component({
  selector: 'app-post-card',
  standalone: true,
  imports: [CommonModule, FormsModule, UserProfile, SafeHtmlPipe],
  templateUrl: './post-card.html',
  styleUrl: './post-card.scss',
})
export class PostCard {
  private session = inject(SessionService);
  private postsService = inject(PostsService);

  post = input.required<Post>();
  showContent = input(false);
  mode = input<'feed' | 'detail'>('feed');

  open = output<number>();
  like = output<void>();
  comment = output<string>();
  delete = output<void>();
  report = output<string>();

  // Signals
  comments = signal<Comment[]>([]);
  isMenuOpen = signal(false);
  isReporting = signal(false);
  commentText = signal('');
  reportText = signal('');

  isLoadingComments = signal(false);
  showComments = signal(false);
  currentPage = signal(0);
  hasMore = signal(false);

  readonly currentUser = this.session.user;

  constructor() {
    effect(() => {
      const currentMode = this.mode();
      const postId = this.post().id;

      untracked(() => {
        if (currentMode === 'detail') {
          this.showComments.set(true);
          if (this.comments().length === 0) this.loadComments();
        }
      });
    });
  }

  readonly canDelete = computed(() => {
    const user = this.currentUser();
    const post = this.post();
    if (!user || !post) return false;
    return String(post.actor.id) === String(user.id) || user.role === 'ADMIN';
  });

  toggleComments() {
    this.showComments.update((v) => !v);
    if (this.showComments() && this.comments().length === 0) {
      this.loadComments();
    }
  }

  loadComments(loadMore = false) {
    if (this.isLoadingComments()) return;

    const pageToFetch = loadMore ? this.currentPage() + 1 : 0;
    this.isLoadingComments.set(true);

    this.postsService.getCommentsByPost(this.post().id, { page: pageToFetch, size: 10 }).subscribe({
      next: (res) => {        
        this.comments.update((prev) => {
          const newData = res.data;
          if (!loadMore) return newData;

          const existingIds = new Set(prev.map((c) => c.id));
          const uniqueNewData = newData.filter((c) => !existingIds.has(c.id));

          return [...prev, ...uniqueNewData];
        });

        this.currentPage.set(res.page);
        this.hasMore.set(!res.isLast);
        this.isLoadingComments.set(false);
      },
      error: () => this.isLoadingComments.set(false),
    });
  }

  submitComment() {
    const text = this.commentText().trim();
    if (text) {
      this.comment.emit(text);
      this.commentText.set('');
      const newComment: Comment = {
        id: Math.random(), // Temp ID
        postId: this.post().id,
        content: text,
        createdAt: new Date().toISOString(),
        actor: this.currentUser()!,
      };
      console.log(newComment.createdAt);
      
      this.comments.update((prev) => [newComment, ...prev]);
    }
  }

  onReportSubmit() {
    const text = this.reportText().trim();
    if (text) {
      this.report.emit(text);
      this.reportText.set('');
      this.isReporting.set(false);
    }
  }

  

  toggleMenu(event: Event) {
    event.preventDefault();
    event.stopPropagation();
    this.isMenuOpen.update((v) => !v);
  }

  openPost() {
    if (this.mode() === 'feed') this.open.emit(this.post().id);
  }

  onDeleteClick() {
    if (confirm('Are you sure you want to delete this post?')) this.delete.emit();
  }
}
