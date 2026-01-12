import { Injectable, inject } from '@angular/core';
import { PostsService } from '../../../../core/services/posts.service';
import { FeedFacade } from '../../feed/feed.facade';
import { CreatePostRequest } from '../../../../core/apis/posts/modles';
import { signal } from '@angular/core';
import { SessionService } from '../../../../core/services/session.service';

@Injectable()
export class PostsEditorFacade {
  private postsService = inject(PostsService);
  private feedFacade = inject(FeedFacade);

  validationError = signal<string | null>(null);
  isSubmitting = signal(false);

  uploadImage(file: File) {
    return this.postsService.uploadImage(file);
  }

  createPost(postData: { title: string; content: string }) {
    if (!postData.title?.trim()) {
      this.validationError.set('Title cannot be empty');
      return;
    }

    if (postData.title.length < 3) {
      this.validationError.set('Title must be at least 3 characters');
      return;
    }

    if (postData.title.length > 500) {
      this.validationError.set('Title cannot exceed 500 characters');
      return;
    }

    if (!postData.content?.trim()) {
      this.validationError.set('Content cannot be empty');
      return;
    }

    if (postData.content.length < 3) {
      this.validationError.set('Content must be at least 3 characters');
      return;
    }

    if (postData.content.length > 10000) {
      this.validationError.set('Content is too long');
      return;
    }

    this.validationError.set(null);
    this.isSubmitting.set(true);

    const request: CreatePostRequest = {
      title: postData.title.trim(),
      content: postData.content.trim(),
    };

    this.isSubmitting.set(true);
    this.postsService.createPost(request).subscribe({
      next: (newPost) => {
        this.feedFacade.addPost(newPost);
        this.isSubmitting.set(false);
      },
      error: (err) => {
        console.error('Create post failed', err);
        this.validationError.set('Failed to create post. Try again later.');
        this.isSubmitting.set(false);
      },
      complete: () => this.isSubmitting.set(false),
    });
  }
}
