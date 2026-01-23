export interface FollowResponse {
  message: string;
}

export interface UpdateProfileRequest {
  displayName?: string;
  bio?: string;
  avatar?: string;
  location?: string;
  cover?: string;
  social?: {
    website?: string;
    twitter?: string;
    github?: string;
    linkedin?: string;
  };
}
