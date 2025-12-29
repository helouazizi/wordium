package com.wordium.posts.exeptions;

import org.springframework.http.ProblemDetail;



public class ExternalServiceProblemException extends RuntimeException {

    private final ProblemDetail problemDetail;

    public ExternalServiceProblemException(ProblemDetail problemDetail) {
        this.problemDetail = problemDetail;
    }

    public ProblemDetail getProblemDetail() {
        return problemDetail;
    }
}