package com.meetingrooms.infrastructure.entrypoints.rest;

import java.time.OffsetDateTime;

public record CreateReservationRequest(Long roomId, OffsetDateTime startAt, OffsetDateTime endAt) {
}
