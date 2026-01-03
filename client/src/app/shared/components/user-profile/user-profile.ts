import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';

@Component({
  selector: 'app-user-profile',
  imports: [CommonModule, MatIconModule, MatMenuModule, MatButtonModule],
  templateUrl: './user-profile.html',
  styleUrl: './user-profile.scss',
})
export class UserProfile {
  @Output() profileClick = new EventEmitter<void>();
  @Input() name!: string; // Display name
  @Input() avatarUrl!: string; // Profile picture
  @Input() role?: string; // Optional role
}
