export interface User {
  id: string;
  username: string;
  role: string;
  email?: string;
  avatarUrl?: string;
  handle?: string;
  bio?: string;
  location?: string;
}

export enum UserRole {
  ADMIN = 'ADMIN',
  USER = 'USER',
  EDITOR = 'EDITOR',
}
