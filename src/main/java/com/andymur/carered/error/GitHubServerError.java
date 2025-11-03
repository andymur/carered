package com.andymur.carered.error;

public class GitHubServerError extends RuntimeException {

    public GitHubServerError(Exception rootCause) {
        super(rootCause);
    }
}
