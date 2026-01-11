import { Component, inject, signal, effect } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { marked } from 'marked';
import DOMPurify from 'dompurify';
import { PostsEditorFacade } from './blog-editor.hacade';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-blog-editor',
  templateUrl: './blog-editor.html',
  styleUrls: ['./blog-editor.scss'],
  standalone: true,
  imports: [CommonModule],
  providers: [PostsEditorFacade],
})
export class BlogEditor {
  private facade = inject(PostsEditorFacade);
  private sanitizer = inject(DomSanitizer);

  markdown = signal('');
  savedMarkdown = signal('');
  previewHtml = signal<SafeHtml>('');

  isOpen = signal(false);
  viewMode = signal<'write' | 'preview'>('write');
  isUploading = signal(false);

  constructor() {
    effect(() => {
      this.renderPreview(this.markdown());
    });
  }

  private async renderPreview(md: string) {
    const raw = await marked.parse(md);
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
            : `\n![Image Description](${url})\n`;

        this.markdown.update((m) => m + block);
        this.isUploading.set(false);
      },
      error: () => this.isUploading.set(false),
    });
  }

  insertFormat(type: string) {
    const formats: Record<string, string> = {
      bold: '****',
      italic: '__',
      list: '\n- ',
    };
    this.markdown.update((m) => m + formats[type]);
  }

  saveBlog() {
    this.facade.savePostContent(this.markdown());
    this.savedMarkdown.set(this.markdown());
    this.isOpen.set(false);
  }

  cancelEdit() {
    this.markdown.set(this.savedMarkdown());
    this.isOpen.set(false);
  }
}
