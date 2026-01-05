import { Component } from '@angular/core';
import { FeedSkeleton } from '../../shared/components/feed-skeleton/feed-skeleton';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-feed',
  imports: [FeedSkeleton, NgIf],
  templateUrl: './feed.html',
  styleUrl: './feed.scss',
})
export class Feed {
  loading = true;
}
