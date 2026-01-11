import { User } from '../../../shared/models/user';

export interface Post {
  id: number;
  title: string;
  content: string;
  actor: User;
  images: PostImage[];
  likesCount: number;
  commentsCount: number;
  reportsCount: number;
  isReported: boolean;
  isFlagged: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface PostImage {
  id: number;
  url: string;
  altText: string;
  displayOrder: number;
}

export interface SignatureData {
  folder: string;
  apiKey: string;
  signature: string;
  cloudName: string;
  timestamp: number;
  upload_preset: string;
}

export interface SignatureResponse {
  data: SignatureData;
}
