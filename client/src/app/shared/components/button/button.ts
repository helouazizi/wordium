import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-button',
  standalone: true,
  imports: [CommonModule],
  template: `<button [class]="type">{{ label }}</button>`,
  styleUrls: ['./button.scss'],
})
export class Button {
  @Input() label!: string;
  @Input() type: 'primary' | 'secondary' = 'primary';
}
