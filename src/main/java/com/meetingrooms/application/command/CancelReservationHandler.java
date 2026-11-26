package com.meetingrooms.application.command;

import com.meetingrooms.application.ConflictException;
import com.meetingrooms.application.NotFoundException;
import com.meetingrooms.domain.model.Reservation;
import com.meetingrooms.domain.model.ReservationStatus;
import com.meetingrooms.domain.model.port.ReservationCommandPort;
import com.meetingrooms.shared.ErrorMessages;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class CancelReservationHandler {

    private final ReservationCommandPort reservationCommandPort;

    public CancelReservationHandler(ReservationCommandPort reservationCommandPort) {
        this.reservationCommandPort = reservationCommandPort;
    }

    @Transactional
    public Reservation handle(CancelReservationCommand command) {
        if (command.reservationId() == null) {
            throw new NotFoundException(ErrorMessages.RESERVATION_NOT_FOUND);
        }

        Reservation reservation = reservationCommandPort.findById(command.reservationId())
                .orElseThrow(() -> new NotFoundException(ErrorMessages.RESERVATION_NOT_FOUND));

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new ConflictException(ErrorMessages.RESERVATION_ALREADY_CANCELLED);
        }

        String actor = command.actorUsername();
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setCancelledBy(actor);
        reservation.setCancelledAt(Instant.now());
        reservation.setUpdatedBy(actor);
        reservation.setUpdatedAt(Instant.now());
        return reservationCommandPort.save(reservation);
    }
}
