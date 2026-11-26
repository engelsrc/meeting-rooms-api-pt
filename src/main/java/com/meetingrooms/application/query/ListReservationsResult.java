package com.meetingrooms.application.query;

import com.meetingrooms.domain.model.Reservation;

import java.util.List;

public record ListReservationsResult(List<Reservation> items, int page, int size, long total) {
}
