import { User } from '../../../shared/models/user.model';

export interface Post {
  id: number;
  title: string;
  content: string;
  actor: User;
  likesCount: number;
  commentsCount: number;
  reportsCount: number;
  isLiked: boolean;
  isBookmarked: boolean;
  isReported: boolean;
  isFlagged: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface SignatureData {
  folder: string;
  apiKey: string;
  signature: string;
  cloudName: string;
  timestamp: number;
  upload_preset: string;
  context: string;
}

export interface SignatureResponse {
  data: SignatureData;
}

export interface CreatePostRequest {
  title: string;
  content: String;
  media?: MediaRequest[];
}

export type Reaction = 'like' | 'unlike';
export type ReportType = 'post' | 'user';
export interface Comment {
  id: number;
  postId: number;
  content: string;
  createdAt: string;
  actor: User;
}

export type MediaType = 'IMAGE' | 'VIDEO';

export interface MediaRequest {
  publicId: string;
  type: MediaType;
}
