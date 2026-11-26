package com.meetingrooms.application.command;

import com.meetingrooms.application.ConflictException;
import com.meetingrooms.application.ValidationException;
import com.meetingrooms.domain.model.Room;
import com.meetingrooms.domain.model.port.RoomCommandPort;
import com.meetingrooms.shared.ErrorMessages;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class CreateRoomHandler {

    private static final int NAME_MAX = 100;
    private static final int LOCATION_MAX = 255;

    private final RoomCommandPort roomCommandPort;

    public CreateRoomHandler(RoomCommandPort roomCommandPort) {
        this.roomCommandPort = roomCommandPort;
    }

    @Transactional
    public Room handle(CreateRoomCommand command) {
        String name = command.name() == null ? null : command.name().trim();
        if (name == null || name.isEmpty()) {
            throw new ValidationException(ErrorMessages.ROOM_NAME_REQUIRED);
        }
        if (name.length() > NAME_MAX) {
            throw new ValidationException(ErrorMessages.ROOM_NAME_TOO_LONG);
        }
        if (command.capacity() <= 0) {
            throw new ValidationException(ErrorMessages.ROOM_CAPACITY_INVALID);
        }
        String location = command.location();
        if (location != null && location.length() > LOCATION_MAX) {
            throw new ValidationException(ErrorMessages.ROOM_LOCATION_TOO_LONG);
        }
        if (roomCommandPort.existsByName(name)) {
            throw new ConflictException(ErrorMessages.ROOM_NAME_ALREADY_EXISTS);
        }

        Room room = new Room(name, command.capacity(), location);
        room.setCreatedBy(command.actorUsername());
        room.setCreatedAt(Instant.now());
        room.setDeleted(false);
        return roomCommandPort.save(room);
    }
}
