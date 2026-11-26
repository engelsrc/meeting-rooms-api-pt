package com.meetingrooms.application;

public class NotFoundException extends ApplicationException {

    public NotFoundException(String messageKey) {
        super(messageKey);
    }
}
