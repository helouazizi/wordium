// src/app/core/models/problem-detail.ts
export interface ProblemDetail {
  type?: string;
  title?: string;
  status?: number;
  detail?: string;
  instance?: string;
  fieldErrors?: Record<string, string[]>;
}
