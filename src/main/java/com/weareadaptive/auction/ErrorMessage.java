package com.weareadaptive.auction;

// TODO: Look into this syntax more
public enum ErrorMessage {
    BAD_REQUEST("Bad request: invalid parameters."),
    OK("Content OK."),
    NOT_FOUND("Not found."),
    CREATED("Resource Created."), FORBIDDEN("Not authorised.");

    private String message;

    ErrorMessage(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
