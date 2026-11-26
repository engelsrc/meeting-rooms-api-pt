package com.meetingrooms.application.command;

public record CancelReservationCommand(Long reservationId, String actorUsername) {
}
