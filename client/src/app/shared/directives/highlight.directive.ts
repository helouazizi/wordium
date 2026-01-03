import { Directive, ElementRef, Input, OnInit } from '@angular/core';

@Directive({
  selector: '[appHighlight]'
})
export class HighlightDirective implements OnInit {
  @Input() appHighlight = false;

  constructor(private el: ElementRef) {}

  ngOnInit() {
    if (this.appHighlight) {
      this.el.nativeElement.style.backgroundColor = '#e0f7fa';
    }
  }
}


// example usage

// <div *ngFor="let post of posts" [appHighlight]="post.isNew">
//   {{ post.content }}
// </div>