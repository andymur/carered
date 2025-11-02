package com.andymur.carered.error;

public class UnsupportedLanguageException extends RuntimeException {

    private final String language;

    public UnsupportedLanguageException(String language) {
        this.language = language;
    }

    @Override
    public String getMessage() {
        return language + " is not supported";
    }
}
