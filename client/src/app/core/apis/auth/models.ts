export interface LoginRequest {
  email: string | null;
  username: string | null;
  password: string;
}

export interface LoginResponse {
  token: string;
}
