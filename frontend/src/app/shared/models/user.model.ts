export interface User {
  id: string;
  username: string;
  role: string;
  email?: string;
  avatar?: string;
  handle?: string;
  bio?: string;
  location?: string;
  createdAt:String;
  cover:string
}

export enum UserRole {
  ADMIN = 'ADMIN',
  USER = 'USER',
}
