import { Component, inject, input, OnInit, signal } from '@angular/core';
import { PostsService } from '../../../core/services/posts.service';
import { SessionService } from '../../../core/services/session.service';
import { CommonModule } from '@angular/common';
import { PostCard } from '../post-card/post-card';
import { Post } from '../../../core/apis/posts/modles';

@Component({
  selector: 'app-posts-list',
  imports: [CommonModule, PostCard],
  templateUrl: './posts-list.html',
  styleUrl: './posts-list.scss',
})
export class PostsList implements OnInit {
  private postsService = inject(PostsService);
  private session = inject(SessionService);

  source = input<'feed' | 'profile'>('feed');
  userId = input<number | null>(null);

  posts = signal<Post[]>([]);
  isLoading = signal(false);
  page = signal(0);

  ngOnInit() {
    this.loadPosts();
  }

  loadPosts() {
    this.isLoading.set(true);
    const request =
      this.source() === 'feed'
        ? this.postsService.getPosts({ page: this.page(), size: 10 })
        : this.postsService.getUserPosts(this.userId()!, { page: this.page(), size: 10 });

    request.subscribe({
      next: (res) => {
        this.posts.set(res.data);
        this.isLoading.set(false);
      },
      error: () => this.isLoading.set(false),
    });
  }

  checkIfCanDelete(post: Post): boolean {
    const user = this.session.user();
    if (!user) return false;
    return String(post.actor.id) === String(user.id) || user.role === 'ADMIN';
  }

  // --- Handlers for Actions emitted by PostCard ---

  onLike(postId: number) {
    this.postsService.likePost(postId).subscribe();
    // Logic to update local signal state if needed
  }

  onDelete(postId: number) {
    this.postsService.deletePost(postId).subscribe(() => {
      this.posts.update((all) => all.filter((p) => p.id !== postId));
    });
  }

  onComment(postId: number, text: string) {
    this.postsService.addComment(postId, text).subscribe((newComment) => {
      // Logic to update the post's comment count or list locally
    });
  }
}
