package com.meetingrooms.application;

public class ValidationException extends ApplicationException {

    public ValidationException(String messageKey) {
        super(messageKey);
    }
}
