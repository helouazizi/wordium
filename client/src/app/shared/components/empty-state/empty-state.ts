import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-empty-state',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './empty-state.html',
})
export class EmptyState {
  @Input() icon = 'bi-inbox';
  @Input() title = 'Nothing here';
  @Input() description = 'There is no content to show right now.';
}
