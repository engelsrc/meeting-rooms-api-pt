package com.meetingrooms.application.query;

import com.meetingrooms.domain.model.Room;

import java.util.List;

public record ListRoomsResult(List<Room> items, int page, int size, long total) {
}
