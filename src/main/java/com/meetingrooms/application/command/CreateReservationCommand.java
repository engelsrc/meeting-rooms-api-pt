package com.meetingrooms.application.command;

import java.time.OffsetDateTime;

public record CreateReservationCommand(Long roomId, OffsetDateTime startAt, OffsetDateTime endAt) {
}
