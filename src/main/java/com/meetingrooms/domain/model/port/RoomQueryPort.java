package com.meetingrooms.domain.model.port;

import com.meetingrooms.domain.model.Room;

import java.util.List;

public interface RoomQueryPort {

    List<Room> findActive(int page, int size);

    long countActive();
}
