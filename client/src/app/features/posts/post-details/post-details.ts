import { Component, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PostsService } from '../../../core/services/posts.service';
import { Post } from '../../../core/apis/posts/modles';
import { FeedFacade } from '../feed/feed.facade';

import { PostCard } from '../../../shared/components/post-card/post-card';

@Component({
  selector: 'app-post-details',
  templateUrl: './post-details.html',
  styleUrls: ['./post-details.scss'],
  standalone: true,
  imports: [PostCard],
})
export class PostDetails implements OnInit {
  private route = inject(ActivatedRoute);
  private feedFacade = inject(FeedFacade);
  private postsService = inject(PostsService);
  post = signal<Post>({} as Post);

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    const cachedPost = this.feedFacade.getPostById(id);

    if (cachedPost) {
      this.post.set(cachedPost);
    } else {
      this.postsService.getPostById(id).subscribe((p) => this.post.set(p));
    }
  }
}
