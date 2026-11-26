package com.meetingrooms.infrastructure.entrypoints.rest;

import com.meetingrooms.domain.model.Reservation;

final class ReservationMapper {

    private ReservationMapper() {
    }

    static ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getRoomId(),
                reservation.getResponsible(),
                reservation.getStartAt(),
                reservation.getEndAt(),
                reservation.getStatus().name()
        );
    }
}
