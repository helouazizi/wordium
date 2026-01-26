import { Component, inject, signal, computed, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { PostList } from '../../shared/components/post-list/post-list';

@Component({
  selector: 'app-post-detail',
  standalone: true,
  imports: [CommonModule, MatProgressSpinnerModule, PostList],
  templateUrl: './post-detail.html',
  styleUrls: ['./post-detail.scss'],
})
export class PostDetail {}
