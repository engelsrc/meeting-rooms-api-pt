package com.meetingrooms.domain.model.port;

import com.meetingrooms.domain.model.Reservation;

import java.time.OffsetDateTime;
import java.util.List;

public interface ReservationQueryPort {

    boolean roomExists(Long roomId);

    List<Reservation> findFiltered(
            Long roomId,
            OffsetDateTime startFromInclusive,
            OffsetDateTime startToExclusive,
            int page,
            int size
    );

    long countFiltered(
            Long roomId,
            OffsetDateTime startFromInclusive,
            OffsetDateTime startToExclusive
    );
}
