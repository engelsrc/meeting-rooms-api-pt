package com.meetingrooms.application.query;

public record ListReservationsQuery(Long roomId, String date, Integer page, Integer size) {
}
