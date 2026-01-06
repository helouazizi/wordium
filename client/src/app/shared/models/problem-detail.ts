// src/app/core/models/problem-detail.ts or shared/models/problem-detail.ts
export interface FieldErrorItem {
  field: string;
  message: string;
}

export interface ProblemDetail {
  type?: string;
  title?: string;
  status?: number;
  detail?: string;
  instance?: string;
  fieldErrors?: FieldErrorItem[];  // ← Changed: now an array
}