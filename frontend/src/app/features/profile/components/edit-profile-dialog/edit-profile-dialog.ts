import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, UntypedFormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { User } from '../../../../shared/models/user.model';

@Component({
  selector: 'app-edit-profile-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
    FormsModule,
    MatIconModule
  ],
  templateUrl: './edit-profile-dialog.html',
  styleUrl: './edit-profile-dialog.scss'
})
export class EditProfileDialogComponent {
  private dialogRef = inject(MatDialogRef<EditProfileDialogComponent>);
  public data = inject<{ type: 'profile' | 'cover' | 'avatar' | 'about', user: User }>(MAT_DIALOG_DATA);
  private fb = inject(UntypedFormBuilder);

  editForm = this.fb.group({
    username: [this.data.user.username, [Validators.required,Validators.minLength(6)]],
    handle: [this.data.user.handle],
    bio: [this.data.user.bio],
    location: [this.data.user.location],
    avatar: [this.data.user.avatar],
    cover: [this.data.user.cover]
  });

  save() {
    if (this.editForm.valid) {
      // In a real app, you'd call your AuthService or ProfileService here
      this.dialogRef.close(this.editForm.value);
    }
  }

  close() {
    this.dialogRef.close();
  }
}