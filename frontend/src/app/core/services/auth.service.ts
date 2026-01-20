import { Injectable, signal } from '@angular/core';
import { User } from '../../shared/models/user.model';

// Define what a User looks like


@Injectable({ providedIn: 'root' })
export class AuthService {
  // Use a Signal to store the user. 
  // This allows components to react instantly when the user logs in/out.
  currentUser = signal<User | null>({
    id: '1',
    username: 'John Doe',
    role:"admin",
    email: 'john@example.com',
    avatarUrl: 'https://api.dicebear.com/7.x/avataaars/svg?seed=John'
  });

  logout() {
    this.currentUser.set(null);
  }
}