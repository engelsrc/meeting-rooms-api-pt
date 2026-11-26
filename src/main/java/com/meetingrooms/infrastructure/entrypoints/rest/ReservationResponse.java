package com.meetingrooms.infrastructure.entrypoints.rest;

import java.time.OffsetDateTime;

public record ReservationResponse(
        Long id,
        Long roomId,
        String responsible,
        OffsetDateTime startAt,
        OffsetDateTime endAt,
        String status
) {
}
