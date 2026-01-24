export interface FollowResponse {
  message: string;
}

export interface UpdateProfileRequest {
  displayName?: string;
  bio?: string;
  avatar?: string;
  location?: string;
  cover?: string;
  avatarPublicId?: string;
  coverPublicId?: string;
  social?: {
    website?: string;
    twitter?: string;
    github?: string;
    linkedin?: string;
  };
}
