package com.weareadaptive.auction;

// TODO: Look into this syntax more
public enum ResponseStatus {
    BAD_REQUEST("Bad request: invalid parameters."),
    OK("Content OK."),
    NOT_FOUND("Not found."),
    CREATED("Resource Created."),
    FORBIDDEN("Not authorised."),
    UNAUTHORIZED("Not authenticated."),
    BAD_CREDENTIALS("Incorrect credentials.");

    private String message;

    ResponseStatus(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
