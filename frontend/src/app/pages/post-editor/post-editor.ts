import { Component, inject, signal, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDividerModule } from '@angular/material/divider';
import { MarkdownModule } from 'ngx-markdown';
import { PostService } from '../../core/services/post.service';
import { NotificationService } from '../../core/services/notification.service';
import { MatSpinner } from '@angular/material/progress-spinner';
import { SafeHtmlPipe } from '../../shared/pipes/safe-html.pipe';
import { MediaRequest, MediaType, Post } from '../../core/apis/posts/post.model';
import { Router } from '@angular/router';
const MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
const MAX_VIDEO_SIZE = 50 * 1024 * 1024; // 50MB
@Component({
  selector: 'app-post-editor',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatIconModule,
    MatDividerModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressBarModule,
    MatTooltipModule,
    MarkdownModule,
    MatSpinner,
    SafeHtmlPipe,
  ],
  templateUrl: './post-editor.html',
  styleUrl: './post-editor.scss',
})
export class PostEditor {
  private postService = inject(PostService);
  private notify = inject(NotificationService);
  private router = inject(Router);
  post!: Post;

  @ViewChild('editorTextarea') textarea!: ElementRef<HTMLTextAreaElement>;

  isEditMode = signal(false);

  title = signal('');
  content = signal('');
  viewMode = signal<'edit' | 'preview'>('edit');
  isUploading = signal(false);
  isSubmitting = signal(false);
  media: MediaRequest[] = [];

  constructor() {
    const nav = this.router.getCurrentNavigation();
    this.post = nav?.extras.state?.['post'];
    if (this.post != null) {
      this.isEditMode.set(true);
      this.title.set(this.post.title);
      this.content.set(this.post.content);
    }
  }

 async  onMediaUpload(event: Event) {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (!file) return;
    this.isUploading.set(true);

    // Strict body check
    const valid = await this.isValidMedia(file);
    if (!valid) {
      this.isUploading.set(false);
      this.notify.showError('File content does not match an image or video');
      return;
    }

    const isImage = file.type.startsWith('image/');
    const isVideo = file.type.startsWith('video/');

    if (!isImage && !isVideo) {
      this.notify.showError('Only images and videos are allowed');
      return;
    }

    if (isImage && file.size > MAX_IMAGE_SIZE) {
      this.notify.showError('Image size must be less than 5MB');
      return;
    }

    if (isVideo && file.size > MAX_VIDEO_SIZE) {
      this.notify.showError('Video size must be less than 50MB');
      return;
    }
    this.isUploading.set(true);

    this.postService.uploadImage(file).subscribe({
      next: (res: any) => {
        const url = res.secure_url;

        const mediaType: MediaType = res.resource_type === 'video' ? 'VIDEO' : 'IMAGE';

        this.media.push({
          publicId: res.public_id,
          type: mediaType,
        });

        const markdownLink =
          mediaType === 'VIDEO'
            ? `\n<video controls src="${url}"></video>\n`
            : `\n![Image preview](${url})\n`;

        this.insertAtCursor(markdownLink);
        this.isUploading.set(false);
        this.notify.showSuccess('Media added to editor');
      },
      error: () => {
        this.isUploading.set(false);
        this.notify.showError('Media upload failed');
      },
    });
  }

  insertFormat(type: 'bold' | 'italic' | 'list' | 'quote' | 'code' | 'link') {
    const syntax: Record<string, string> = {
      bold: '****',
      italic: '__',
      list: '\n- ',
      quote: '\n> ',
      code: '\n```\n\n```',
      link: '[Text](https://)',
    };
    this.insertAtCursor(syntax[type]);
  }

  private insertAtCursor(text: string) {
    if (this.viewMode() === 'preview') this.viewMode.set('edit');

    const el = this.textarea.nativeElement;
    const start = el.selectionStart;
    const end = el.selectionEnd;
    const currentContent = this.content();

    const newContent = currentContent.substring(0, start) + text + currentContent.substring(end);
    this.content.set(newContent);

    setTimeout(() => {
      el.focus();
      const cursorOffset =
        text.includes('**') || text.includes('__') ? text.length / 2 : text.length;
      el.setSelectionRange(start + cursorOffset, start + cursorOffset);
    });
  }

  submitPost() {
    if (!this.title().trim() || !this.content().trim()) {
      this.notify.showError('Title and Content are required');
      return;
    }

    const usedPublicIds = this.extractUsedPublicIds(this.content());

    const filteredMedia = this.media.filter((m) => usedPublicIds.has(m.publicId));

    this.isSubmitting.set(true);

    const request$ = this.isEditMode()
      ? this.postService.updatePost({
          id: this.post.id,
          title: this.title(),
          content: this.content(),
          media: filteredMedia,
        })
      : this.postService.createPost({
          title: this.title(),
          content: this.content(),
          media: filteredMedia,
        });

    request$.subscribe({
      next: () => {
        this.notify.showSuccess('Post published successfully!');
        this.title.set('');
        this.content.set('');
        this.media = [];
        this.isSubmitting.set(false);
      },
      error: () => this.isSubmitting.set(false),
    });
  }

  cancel() {
    if (confirm('Discard all changes?')) {
      this.title.set('');
      this.content.set('');
      this.media = [];
    }
  }

  private extractUsedPublicIds(content: string): Set<string> {
    const used = new Set<string>();

    const urlRegex =
      /https:\/\/res\.cloudinary\.com\/[^\/]+\/(?:image|video)\/upload\/[^\/]+\/([^.\s)]+)/g;

    let match: RegExpExecArray | null;

    while ((match = urlRegex.exec(content)) !== null) {
      used.add(match[1]);
    }

    return used;
  }

  private async isValidMedia(file: File): Promise<boolean> {
    const allowedImageHeaders: Record<string, string[]> = {
      png: ['89504e47'],
      jpg: ['ffd8ff'],
      gif: ['47494638'],
    };
    const allowedVideoHeaders: Record<string, string[]> = {
      mp4: ['00000018', '00000020'], // common MP4 start bytes
      webm: ['1a45dfa3'],
    };

    // Read first 12 bytes of the file
    const buffer = await file.arrayBuffer();
    const arr = new Uint8Array(buffer.slice(0, 12));
    const header = Array.from(arr)
      .map((b) => b.toString(16).padStart(2, '0'))
      .join('');

    // Check against image headers
    for (const headers of Object.values(allowedImageHeaders)) {
      if (headers.some((h) => header.startsWith(h))) return true;
    }

    // Check against video headers
    for (const headers of Object.values(allowedVideoHeaders)) {
      if (headers.some((h) => header.startsWith(h))) return true;
    }

    return false;
  }
}
