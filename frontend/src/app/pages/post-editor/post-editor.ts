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
import { switchMap } from 'rxjs';
import { MatSpinner } from '@angular/material/progress-spinner';
import { SafeHtmlPipe } from '../../shared/pipes/safe-html.pipe';

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

  @ViewChild('editorTextarea') textarea!: ElementRef<HTMLTextAreaElement>;

  title = signal('');
  content = signal('');
  viewMode = signal<'edit' | 'preview'>('edit');
  isUploading = signal(false);
  isSubmitting = signal(false);

  onMediaUpload(event: Event) {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (!file) return;

    this.isUploading.set(true);

    this.postService
      .getSignature()
      .pipe(switchMap((sig) => this.postService.uploadToCloudinary(file, sig.data)))
      .subscribe({
        next: (res: any) => {
          const url = res.secure_url;
          const markdownLink =
            res.resource_type === 'video'
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

    this.isSubmitting.set(true);
    this.postService
      .createPost({
        title: this.title(),
        content: this.content(),
      })
      .subscribe({
        next: () => {
          this.notify.showSuccess('Post published successfully!');
          this.title.set('');
          this.content.set('');
          this.isSubmitting.set(false);
        },
        error: () => this.isSubmitting.set(false),
      });
  }

  cancel() {
    if (confirm('Discard all changes?')) {
      this.title.set('');
      this.content.set('');
      // Optionally navigate away:
      // this.router.navigate(['/feed']);
    }
  }
}
