package com.meetingrooms.infrastructure.entrypoints.rest;

import com.meetingrooms.domain.model.Room;

final class RoomMapper {

    private RoomMapper() {
    }

    static RoomResponse toResponse(Room room) {
        return new RoomResponse(room.getId(), room.getName(), room.getCapacity(), room.getLocation());
    }
}
