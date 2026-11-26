package com.meetingrooms.shared;

public final class ErrorMessages {

    public static final String ROOM_NAME_REQUIRED = "Room name is required";
    public static final String ROOM_NAME_TOO_LONG = "Room name must be at most 100 characters";
    public static final String ROOM_CAPACITY_INVALID = "Room capacity must be greater than 0";
    public static final String ROOM_LOCATION_TOO_LONG = "Room location must be at most 255 characters";
    public static final String ROOM_NAME_ALREADY_EXISTS = "Room name already exists";
    public static final String ROOM_NOT_FOUND = "Room not found";
    public static final String INVALID_PAGE_OR_SIZE = "Page must be >= 0 and size must be >= 1";
    public static final String INVALID_RESERVATION_PAGE_OR_SIZE =
            "Page must be >= 0 and size must be between 1 and 100";
    public static final String VALIDATION_FAILED = "Validation failed";

    public static final String RESERVATION_INTERVAL_INVALID = "Reservation endAt must be after startAt";
    public static final String RESERVATION_OVERLAP = "Room is already booked for the selected time range";
    public static final String RESERVATION_NOT_FOUND = "Reservation not found";
    public static final String RESERVATION_ALREADY_CANCELLED = "Reservation is already cancelled";
    public static final String INVALID_RESERVATION_DATE = "Date must be a valid ISO date (yyyy-MM-dd)";
    public static final String INVALID_LIST_ROOM_ID = "Room id does not exist";

    public static final String AUTH_USERNAME_REQUIRED = "Username is required";
    public static final String AUTH_PASSWORD_REQUIRED = "Password is required";
    public static final String AUTH_USER_NOT_FOUND = "User not found";
    public static final String AUTH_INVALID_PASSWORD = "Invalid password";
    public static final String AUTH_UNAUTHENTICATED = "Authentication required";
    public static final String AUTH_INVALID_TOKEN = "Invalid or expired token";

    private ErrorMessages() {
    }
}
