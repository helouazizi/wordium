import { Component, inject, signal, computed, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { PostCard } from '../../shared/components/post-card/post-card';
import { NotFound } from '../../shared/components/not-found/not-found';
import { PostService } from '../../core/services/post.service';
import { AuthService } from '../../core/services/auth.service';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Post } from '../../core/apis/posts/post.model';

@Component({
  selector: 'app-post-detail',
  standalone: true,
  imports: [CommonModule, PostCard, NotFound, MatProgressSpinnerModule],
  templateUrl: './post-detail.html',
  styleUrls: ['./post-detail.scss'],
})
export class PostDetail implements OnInit {
  private route = inject(ActivatedRoute);
  private postService = inject(PostService);
  private authService = inject(AuthService);

  @ViewChild(PostCard) postCard!: PostCard;

  isLoading = signal(true);
  error = signal(false);
  postId = signal<number | null>(null);

  currentUser = this.authService.user();
  post = signal<Post | null>(null);

  ngOnInit() {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = Number(idParam);

    if (!idParam || isNaN(id)) {
      this.error.set(true);
      this.isLoading.set(false);
      return;
    }

    this.postId.set(id);
    this.fetchPost(id);
    this.isLoading.set(false);
  }

  private fetchPost(id: number) {
    this.isLoading.set(true);
    this.postService.fetchPostById(id).subscribe({
      next: (res) => {
        this.post.set(res);
        this.isLoading.set(false);
        this.error.set(false);
      },
      error: () => {
        this.isLoading.set(false);
        this.error.set(true);
      },
    });
  }

  // Action methods left for later implementation as requested
  handleReaction(postId: number) {}
  handleBookmark(postId: number) {}
  handleComment(event: any) {}
  handleRequestComments(event: any) {}
  handleReport(event: any) {}
  handleDelete(postId: number) {}
}
