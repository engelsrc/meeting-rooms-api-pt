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

    private static final String SYSTEM_USER = "system";

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

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setCancelledBy(SYSTEM_USER);
        reservation.setCancelledAt(Instant.now());
        reservation.setUpdatedBy(SYSTEM_USER);
        reservation.setUpdatedAt(Instant.now());
        return reservationCommandPort.save(reservation);
    }
}
