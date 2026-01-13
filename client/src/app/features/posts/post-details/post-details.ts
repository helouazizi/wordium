import { Component, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PostsService } from '../../../core/services/posts.service';
import { Post } from '../../../core/apis/posts/modles';
import { FeedFacade } from '../feed/feed.facade';
import { PostCard } from '../../../shared/components/post-card/post-card';
import { NotFound } from '../../../shared/components/not-found/not-found';
import { SessionService } from '../../../core/services/session.service';

@Component({
  selector: 'app-post-details',
  templateUrl: './post-details.html',
  styleUrls: ['./post-details.scss'],
  standalone: true,
  imports: [PostCard, NotFound],
})
export class PostDetails implements OnInit {
  private route = inject(ActivatedRoute);
  private feedFacade = inject(FeedFacade);
  private postsService = inject(PostsService);
  private session = inject(SessionService);
  post = signal<Post>({} as Post);
  user = this.session.getUser();
  notFound = signal<boolean>(false);

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    const cachedPost = this.feedFacade.getPostById(id);

    if (cachedPost) {
      this.post.set(cachedPost);
    } else {
      this.postsService.getPostById(id).subscribe({
        next: (p) => {
          if (p) {
            this.post.set(p);
          } else {
            this.notFound.set(true);
          }
        },
        error: () => {
          this.notFound.set(true);
        },
      });
    }
  }
}
