import { Component, inject, signal, effect } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { marked } from 'marked';
import DOMPurify from 'dompurify';
import { PostsEditorFacade } from './blog-editor.hacade';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-blog-editor',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './blog-editor.html',
  styleUrls: ['./blog-editor.scss'],
  providers: [PostsEditorFacade],
})
export class BlogEditor {
  private facade = inject(PostsEditorFacade);
  private sanitizer = inject(DomSanitizer);

  isOpen = signal(false);
  viewMode = signal<'write' | 'preview'>('write');
  isUploading = signal(false);
  isSubmitting = this.facade.isSubmitting;
  validationError = this.facade.validationError;
  title = signal('');
  markdown = signal('');
  previewHtml = signal<SafeHtml>('');
  savedTitle = signal('');
  savedMarkdown = signal('');

  constructor() {
    effect(() => {
      const currentTitle = this.title();
      const currentMarkdown = this.markdown();
      this.renderPreview(currentMarkdown);
    });
  }

  private async renderPreview(md: string) {
    if (!md) {
      this.previewHtml.set('');
      return;
    }

    const raw = await marked.parse(md, {
      breaks: true,
      gfm: true,
    });

    const clean = DOMPurify.sanitize(raw);
    this.previewHtml.set(this.sanitizer.bypassSecurityTrustHtml(clean));
  }
  onMediaUpload(event: Event) {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (!file) return;

    this.isUploading.set(true);
    this.facade.uploadImage(file).subscribe({
      next: (res) => {
        const url = res.secure_url;
        const block =
          res.resource_type === 'video'
            ? `\n<video controls src="${url}"></video>\n`
            : `\n![Image](${url})\n`;

        this.markdown.update((m) => m + block);
        this.isUploading.set(false);
      },
      error: () => this.isUploading.set(false),
    });
  }

  insertFormat(type: 'bold' | 'italic' | 'list') {
    const formats: Record<string, string> = {
      bold: '****',
      italic: '__',
      list: '\n- ',
    };
    this.markdown.update((m) => m + formats[type]);
  }

  saveBlog() {
    const postData = {
      title: this.title(),
      content: this.markdown(),
    };

    this.facade.createPost(postData);

    if (!this.validationError()) {
      this.savedTitle.set(this.title());
      this.savedMarkdown.set(this.markdown());
      this.resetEditor();
    }
  }

  cancelEdit() {
    this.title.set(this.savedTitle());
    this.markdown.set(this.savedMarkdown());
    this.resetEditor();
  }

  private resetEditor() {
    this.isOpen.set(false);
    this.viewMode.set('write');
    this.isUploading.set(false);
  }
}
