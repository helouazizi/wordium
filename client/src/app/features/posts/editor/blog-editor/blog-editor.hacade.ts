import { Injectable, inject } from '@angular/core';
import { PostsService } from '../../../../core/services/posts.service';
import { FeedFacade } from '../../feed/feed.facade';
import { CreatePostRequest, Post } from '../../../../core/apis/posts/modles';
import { signal } from '@angular/core';
import { SessionService } from '../../../../core/services/session.service';
import { ToastService } from '../../../../core/services/toast.service';
import { EMPTY, Observable, tap } from 'rxjs';

@Injectable()
export class PostsEditorFacade {
  private postsService = inject(PostsService);
  private session = inject(SessionService);
  private toast = inject(ToastService);

  validationError = signal<string | null>(null);
  isSubmitting = signal(false);
  private user = this.session.getUser();

  uploadImage(file: File) {
    return this.postsService.uploadImage(file);
  }
  createPost(postData: { title: string; content: string }): Observable<Post> {
    if (!postData.title?.trim() || postData.title.length < 3) {
      this.validationError.set('Title is too short');
      return EMPTY; 
    }
    if (!postData.content?.trim() || postData.content.length < 3) {
      this.validationError.set('Content is too short');
      return EMPTY;
    }

    this.validationError.set(null);
    this.isSubmitting.set(true);

    const request: CreatePostRequest = {
      title: postData.title.trim(),
      content: postData.content.trim(),
    };

    return this.postsService.createPost(request).pipe(
      tap({
        next: (res) => {
          res.actor = this.user!
          this.isSubmitting.set(false);
          this.toast.success('Blog created successfully');
        },
        error: (err) => {
          this.isSubmitting.set(false);
          this.validationError.set('Failed to create blog. Try again.');
          this.toast.error('Creation failed');
        },
      }),
    );
  }
}
