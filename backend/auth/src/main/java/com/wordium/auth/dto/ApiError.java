package com.wordium.auth.dto;

import java.time.Instant;
import java.util.List;

public class ApiError {

        private int status;
        private String error;
        private String message;
        private Instant timestamp;

        private List<FieldValidationError> fieldErrors; // optional for form fields

        public void setStatus(int status) {
                this.status = status;
        }

        public void setError(String error) {
                this.error = error;
        }

        public void setMessage(String message) {
                this.message = message;
        }

        public void setTimestamp(Instant timestamp) {
                this.timestamp = timestamp;
        }

        public int getStatus() {
                return status;
        }

        public String getMessage() {
                return message;
        }

        public String getError() {
                return error;
        }

        public Instant getTimestamp() {
                return timestamp;
        }

        public ApiError(int status, String error, String message, Instant timestamp) {
                this.status = status;
                this.error = error;
                this.message = message;
        }

        public ApiError(int status, String error, String message, Instant timestamp,
                        List<FieldValidationError> fieldErrors) {
                this.status = status;
                this.error = error;
                this.message = message;
                this.timestamp = timestamp;
                this.fieldErrors = fieldErrors;
        }

        public static class FieldValidationError {
                private String field;
                private String message;

                public FieldValidationError(String field, String message) {
                        this.field = field;
                        this.message = message;
                }

                public String getField() {
                        return field;
                }

                public String getMessage() {
                        return message;
                }

                public void setField(String field) {
                        this.field = field;
                }

                public void setMessage(String message) {
                        this.message = message;
                }
        }

        public List<FieldValidationError> getFieldErrors() {
                return fieldErrors;
        }

        public void setFieldErrors(List<FieldValidationError> fieldErrors) {
                this.fieldErrors = fieldErrors;
        }
}
