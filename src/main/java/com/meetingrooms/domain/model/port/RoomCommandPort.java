package com.meetingrooms.domain.model.port;

import com.meetingrooms.domain.model.Room;

public interface RoomCommandPort {

    boolean existsByName(String name);

    Room save(Room room);
}
