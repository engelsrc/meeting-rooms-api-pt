package com.meetingrooms.application;

public class UnauthorizedException extends ApplicationException {

    public UnauthorizedException(String messageKey) {
        super(messageKey);
    }
}
