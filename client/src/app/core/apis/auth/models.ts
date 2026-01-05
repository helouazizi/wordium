export interface LoginRequest {
  email: string | null;
  username: string | null;
  password: string;
}

export interface LoginResponse {
  token: string;
}


export interface SignupRequest {
  email: string;
  username: string;
  password: string;
  bio?: string;
  location?: string;
  avatar?: File;
}

