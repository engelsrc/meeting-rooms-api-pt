package com.meetingrooms.application;

public class ConflictException extends ApplicationException {

    public ConflictException(String messageKey) {
        super(messageKey);
    }
}
