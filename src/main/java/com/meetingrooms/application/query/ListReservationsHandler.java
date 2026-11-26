package com.meetingrooms.application.query;

import com.meetingrooms.application.ValidationException;
import com.meetingrooms.domain.model.Reservation;
import com.meetingrooms.domain.model.port.ReservationQueryPort;
import com.meetingrooms.shared.ErrorMessages;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class ListReservationsHandler {

    static final ZoneId BUSINESS_ZONE = ZoneId.of("America/Mexico_City");

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 100;

    private final ReservationQueryPort reservationQueryPort;

    public ListReservationsHandler(ReservationQueryPort reservationQueryPort) {
        this.reservationQueryPort = reservationQueryPort;
    }

    @Transactional(readOnly = true)
    public ListReservationsResult handle(ListReservationsQuery query) {
        int page = query.page() == null ? DEFAULT_PAGE : query.page();
        int size = query.size() == null ? DEFAULT_SIZE : query.size();
        if (page < 0 || size < 1 || size > MAX_SIZE) {
            throw new ValidationException(ErrorMessages.INVALID_RESERVATION_PAGE_OR_SIZE);
        }

        if (query.roomId() != null && !reservationQueryPort.roomExists(query.roomId())) {
            throw new ValidationException(ErrorMessages.INVALID_LIST_ROOM_ID);
        }

        OffsetDateTime startFrom = null;
        OffsetDateTime startTo = null;
        if (query.date() != null && !query.date().isBlank()) {
            try {
                LocalDate day = LocalDate.parse(query.date().trim());
                startFrom = day.atStartOfDay(BUSINESS_ZONE).toOffsetDateTime();
                startTo = day.plusDays(1).atStartOfDay(BUSINESS_ZONE).toOffsetDateTime();
            } catch (DateTimeParseException ex) {
                throw new ValidationException(ErrorMessages.INVALID_RESERVATION_DATE);
            }
        }

        List<Reservation> items = reservationQueryPort.findFiltered(
                query.roomId(), startFrom, startTo, page, size
        );
        long total = reservationQueryPort.countFiltered(query.roomId(), startFrom, startTo);
        return new ListReservationsResult(items, page, size, total);
    }
}
