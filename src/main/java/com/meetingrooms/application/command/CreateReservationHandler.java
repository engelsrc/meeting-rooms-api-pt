package com.meetingrooms.application.command;

import com.meetingrooms.application.ConflictException;
import com.meetingrooms.application.NotFoundException;
import com.meetingrooms.application.ValidationException;
import com.meetingrooms.domain.model.Reservation;
import com.meetingrooms.domain.model.ReservationStatus;
import com.meetingrooms.domain.model.Room;
import com.meetingrooms.domain.model.port.ReservationCommandPort;
import com.meetingrooms.shared.ErrorMessages;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class CreateReservationHandler {

    private final ReservationCommandPort reservationCommandPort;

    public CreateReservationHandler(ReservationCommandPort reservationCommandPort) {
        this.reservationCommandPort = reservationCommandPort;
    }

    @Transactional
    public Reservation handle(CreateReservationCommand command) {
        if (command.roomId() == null || command.startAt() == null || command.endAt() == null) {
            throw new ValidationException(ErrorMessages.VALIDATION_FAILED);
        }
        if (!command.endAt().isAfter(command.startAt())) {
            throw new ValidationException(ErrorMessages.RESERVATION_INTERVAL_INVALID);
        }

        Room room = reservationCommandPort.lockRoomForUpdate(command.roomId())
                .orElseThrow(() -> new NotFoundException(ErrorMessages.ROOM_NOT_FOUND));
        if (room.isDeleted()) {
            throw new NotFoundException(ErrorMessages.ROOM_NOT_FOUND);
        }

        if (reservationCommandPort.existsActiveOverlap(command.roomId(), command.startAt(), command.endAt())) {
            throw new ConflictException(ErrorMessages.RESERVATION_OVERLAP);
        }

        String actor = command.actorUsername();
        Reservation reservation = new Reservation(
                command.roomId(),
                actor,
                command.startAt(),
                command.endAt(),
                ReservationStatus.ACTIVE
        );
        reservation.setCreatedBy(actor);
        reservation.setCreatedAt(Instant.now());
        reservation.setDeleted(false);
        return reservationCommandPort.save(reservation);
    }
}
