package com.andymur.carered.error;

public class IncorrectPageException extends RuntimeException {

    private final int page;
    private final int pageSize;

    public IncorrectPageException(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    @Override
    public String getMessage() {
        return "Payload on page " + page + " with page size " + pageSize + " exceeds supported payload size";
    }
}
