package com.meetingrooms.domain.model.port;

import com.meetingrooms.domain.model.Reservation;
import com.meetingrooms.domain.model.Room;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface ReservationCommandPort {

    Optional<Room> lockRoomForUpdate(Long roomId);

    boolean existsActiveOverlap(Long roomId, OffsetDateTime startAt, OffsetDateTime endAt);

    Reservation save(Reservation reservation);

    Optional<Reservation> findById(Long id);
}
