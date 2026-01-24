import { Component, inject, signal, computed, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { PostCard } from '../../shared/components/post-card/post-card';
import { NotFound } from '../../shared/components/not-found/not-found';
import { PostService } from '../../core/services/post.service';
import { AuthService } from '../../core/services/auth.service';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

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

  post = computed(() => {
    const id = this.postId();
    if (!id) return null;
    return this.postService.posts().find((p) => p.id === id) || null;
  });

  ngOnInit() {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = Number(idParam);

    if (!idParam || isNaN(id)) {
      this.error.set(true);
      this.isLoading.set(false);
      return;
    }

    this.postId.set(id);

    const exists = this.postService.posts().some(p => p.id === id);
    if (!exists) {
      this.fetchPost(id);
    } else {
      this.isLoading.set(false);
    }
  }

  private fetchPost(id: number) {
    this.isLoading.set(true);
    this.postService.fetchPostById(id).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.error.set(false);
      },
      error: () => {
        this.isLoading.set(false);
        this.error.set(true);
      }
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