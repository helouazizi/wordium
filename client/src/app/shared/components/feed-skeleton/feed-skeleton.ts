import { Component, Input } from '@angular/core';
import { NgFor } from '@angular/common';

@Component({
  selector: 'app-feed-skeleton',
  standalone: true,
  imports: [NgFor],
  templateUrl: './feed-skeleton.html',
  styleUrls: ['./feed-skeleton.scss'],
})
export class FeedSkeleton {
  @Input() count = 5;
}
