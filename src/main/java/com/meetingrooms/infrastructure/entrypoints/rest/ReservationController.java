package com.meetingrooms.infrastructure.entrypoints.rest;

import com.meetingrooms.application.NotFoundException;
import com.meetingrooms.application.ValidationException;
import com.meetingrooms.application.command.CancelReservationCommand;
import com.meetingrooms.application.command.CancelReservationHandler;
import com.meetingrooms.application.command.CreateReservationCommand;
import com.meetingrooms.application.command.CreateReservationHandler;
import com.meetingrooms.application.query.ListReservationsHandler;
import com.meetingrooms.application.query.ListReservationsQuery;
import com.meetingrooms.application.query.ListReservationsResult;
import com.meetingrooms.domain.model.Reservation;
import com.meetingrooms.shared.ErrorMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final CreateReservationHandler createReservationHandler;
    private final CancelReservationHandler cancelReservationHandler;
    private final ListReservationsHandler listReservationsHandler;

    public ReservationController(
            CreateReservationHandler createReservationHandler,
            CancelReservationHandler cancelReservationHandler,
            ListReservationsHandler listReservationsHandler
    ) {
        this.createReservationHandler = createReservationHandler;
        this.cancelReservationHandler = cancelReservationHandler;
        this.listReservationsHandler = listReservationsHandler;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@RequestBody CreateReservationRequest request) {
        if (request == null) {
            throw new ValidationException(ErrorMessages.VALIDATION_FAILED);
        }
        Reservation reservation = createReservationHandler.handle(
                new CreateReservationCommand(request.roomId(), request.startAt(), request.endAt()));
        return ResponseEntity.status(HttpStatus.CREATED).body(ReservationMapper.toResponse(reservation));
    }

    @GetMapping
    public PageResponse<ReservationResponse> list(
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        ListReservationsResult result = listReservationsHandler.handle(
                new ListReservationsQuery(roomId, date, page, size));
        List<ReservationResponse> items = result.items().stream().map(ReservationMapper::toResponse).toList();
        return new PageResponse<>(items, result.page(), result.size(), result.total());
    }

    @PostMapping("/cancel")
    public ReservationResponse cancel(@RequestBody CancelReservationRequest request) {
        if (request == null || request.reservationId() == null) {
            throw new NotFoundException(ErrorMessages.RESERVATION_NOT_FOUND);
        }
        Reservation reservation = cancelReservationHandler.handle(
                new CancelReservationCommand(request.reservationId()));
        return ReservationMapper.toResponse(reservation);
    }
}
