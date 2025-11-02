package com.andymur.carered.error;

public class IncorrectPageSizeException extends RuntimeException {

    private final int pageSize;
    private final int supportedPageSize;

    public IncorrectPageSizeException(
            int pageSize,
            int supportedPageSize
    ) {
        this.pageSize = pageSize;
        this.supportedPageSize = supportedPageSize;
    }

    @Override
    public String getMessage() {
        return "The page size " + pageSize + " was given but " + supportedPageSize + " is supported.";
    }
}
