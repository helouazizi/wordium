import { Component, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PostCard } from '../../../shared/components/post-card/post-card';
import { PostDetailsFacade } from './post-detals.facade';
import { NotFound } from '../../../shared/components/not-found/not-found';
import { Loading } from '../../../shared/components/global-loader/global-loader';

@Component({
  selector: 'app-post-details',
  templateUrl: './post-details.html',
  styleUrls: ['./post-details.scss'],
  standalone: true,
  imports: [PostCard,NotFound,Loading],
  providers: [PostDetailsFacade],
})
export class PostDetails implements OnInit {
  protected facade = inject(PostDetailsFacade);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  readonly notFound = this.facade.error;

  readonly post = this.facade.post;

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.facade.setPostId(id);
    }
  }

  onLike(postId: number) {
    this.facade.reactPost(postId);
  }

  onComment(postId: number, content: string) {
    this.facade.addComment(postId, content);
  }

  onReport(postId: number, reason: string) {
    this.facade.reportPost(postId, reason);
  }

  
  onDelete() {
    this.facade.delete().subscribe(() => {
      this.router.navigate(['/']); 
    });
  }
}
