package com.meetingrooms.application;

public abstract class ApplicationException extends RuntimeException {

    private final String messageKey;

    protected ApplicationException(String messageKey) {
        super(messageKey);
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
