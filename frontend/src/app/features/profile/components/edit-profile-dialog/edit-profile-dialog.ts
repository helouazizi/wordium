import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormGroup, FormsModule, ReactiveFormsModule, UntypedFormBuilder, Validators } from '@angular/forms';
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
    MatIconModule,
  ],
  templateUrl: './edit-profile-dialog.html',
  styleUrls: ['./edit-profile-dialog.scss'],
})
export class EditProfileDialogComponent {
  private dialogRef = inject(MatDialogRef<EditProfileDialogComponent>);
  public data = inject<{ type: 'profile' | 'cover' | 'avatar' | 'about' | 'social'; user: User }>(
    MAT_DIALOG_DATA,
  );
  private fb = inject(UntypedFormBuilder);

  editForm = this.fb.group({
    displayName: [this.data.user.displayName],
    bio: [this.data.user.bio],
    location: [this.data.user.location],
    avatar: [this.data.user.avatar],
    cover: [this.data.user.cover],
    social: this.fb.group({
      website: [this.data.user.social?.website || ''],
      twitter: [this.data.user.social?.twitter || ''],
      github: [this.data.user.social?.github || ''],
      linkedin: [this.data.user.social?.linkedin || ''],
    }),
  });
  save() {
    if (this.editForm.valid) {
      const result: any = {};
      switch (this.data.type) {
        case 'profile':
          result.displayName = this.editForm.value.displayName;
          result.location = this.editForm.value.location;
          break;
        case 'about':
          result.bio = this.editForm.value.bio;
          break;
        case 'avatar':
        case 'cover':
          result[this.data.type] = this.editForm.value[this.data.type];
          break;
        case 'social':
          result.social = { ...this.editForm.value.social };
          break;
      }
      this.dialogRef.close(result);
    }
  }

  get socialForm() {
    return this.editForm.get('social') as FormGroup;
  }

  close() {
    this.dialogRef.close();
  }
}
