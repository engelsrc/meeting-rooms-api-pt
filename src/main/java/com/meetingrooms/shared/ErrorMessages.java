package com.meetingrooms.shared;

public final class ErrorMessages {

    public static final String ROOM_NAME_REQUIRED = "Room name is required";
    public static final String ROOM_NAME_TOO_LONG = "Room name must be at most 100 characters";
    public static final String ROOM_CAPACITY_INVALID = "Room capacity must be greater than 0";
    public static final String ROOM_LOCATION_TOO_LONG = "Room location must be at most 255 characters";
    public static final String ROOM_NAME_ALREADY_EXISTS = "Room name already exists";
    public static final String INVALID_PAGE_OR_SIZE = "Page must be >= 0 and size must be >= 1";
    public static final String VALIDATION_FAILED = "Validation failed";

    private ErrorMessages() {
    }
}
