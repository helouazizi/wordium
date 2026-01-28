import { inject, Injectable, signal, computed } from '@angular/core';
import {
  CreatePostRequest,
  Post,
  Reaction,
  ReportType,
  SignatureResponse,
} from '../apis/posts/post.model';
import { catchError, finalize, Observable, of, switchMap, tap, throwError } from 'rxjs';
import { PostsClient } from '../apis/posts/post-client';
import { PageRequest } from '../../shared/models/pagination.model';

@Injectable({ providedIn: 'root' })
export class PostService {
  private readonly client = inject(PostsClient);

  getFeed(params: PageRequest) {
    return this.client.feed(params);
  }
  getUserPosts(userid: number, params: PageRequest) {
    return this.client.getUserPosts(userid, params);
  }

  getPostComments(postId: number, params: PageRequest) {
    return this.client.getPostComments(postId, params);
  }

  getAllPosts(params: PageRequest) {
    return this.client.getAllPosts(params);
  }
  getBookmarks(params: PageRequest) {
    return this.client.getBookmarks(params);
  }

  fetchPostById(id: number): Observable<Post> {
    return this.client.getPostById(id);
  }
  createPost(post: CreatePostRequest): Observable<Post> {
    return this.client.createPost(post);
  }
  getPostById(id: number): Observable<Post> {
    return this.client.getPostById(id);
  }

  reactToPost(postId: number, reaction: Reaction) {
    return this.client.reactPost(postId, reaction);
  }

  deletePost(postId: number) {
    return this.client.deletePost(postId);
  }

  flagPost(postId: number) {
    return this.client.flagPost(postId);
  }

  unflagPost(postId: number) {
    return this.client.unflagPost(postId);
  }

  deleteComment(postId: number, commentId: number) {
    return this.client.deleteComment(postId, commentId);
  }

  addComment(postId: number, content: string) {
    return this.client.addComment(postId, content);
  }

  reportPost(id: number, reason: string) {
    return this.client.reportPost(id, reason);
  }

  reportUser(id: number, reason: string) {
    return this.client.reportUser(id, reason);
  }

  getSignature(): Observable<SignatureResponse> {
    return this.client.getSignature();
  }

  uploadToCloudinary(file: File, sigData: any) {
    return this.client.uploadToCloudinary(file, sigData);
  }

  uploadImage(file: File): Observable<any> {
    return this.getSignature().pipe(switchMap((res) => this.uploadToCloudinary(file, res.data)));
  }
}
