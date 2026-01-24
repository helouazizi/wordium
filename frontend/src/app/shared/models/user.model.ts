export interface User {
  id: number;
  username: string;
  role: string;
  email?: string;
  avatar?: string;
  handle?: string;
  bio?: string;
  location?: string;
}

export enum UserRole {
  ADMIN = 'ADMIN',
  USER = 'USER',
  EDITOR = 'EDITOR',
}
