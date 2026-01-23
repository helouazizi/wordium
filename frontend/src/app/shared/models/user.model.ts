export interface User {
  id: number;
  username: string;
  displayName?: string;

  email: string;
  role: 'USER' | 'ADMIN' ;

  avatar?: string;
  cover?: string;
  bio?: string;
  location?: string;

  createdAt: string;
  updatedAt?: string;
  lastLoginAt?: string;

  isVerified?: boolean;
  isBanned?: boolean;
  isPrivate?: boolean;

  isFollowing?: boolean;
  followsMe?: boolean;

  stats?: {
    followers: number;
    following: number;
    posts: number;
    bookmarks: number;
  };

  social?: {
    website?: string;
    twitter?: string;
    github?: string;
    linkedin?: string;
  };
}
