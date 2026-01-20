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
