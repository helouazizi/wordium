import { Injectable, inject } from '@angular/core';
import { PostsService } from '../../../../core/services/posts.service';

@Injectable()
export class PostsEditorFacade {
  private postsService = inject(PostsService);

  uploadImage(file: File) {
    return this.postsService.uploadImage(file);
  }

  savePostContent(json: any) {
    // later you’ll call postsService.createPost(json)
    console.log('Facade received content', json);
  }
}
