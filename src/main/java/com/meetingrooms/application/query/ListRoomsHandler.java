package com.meetingrooms.application.query;

import com.meetingrooms.application.ValidationException;
import com.meetingrooms.domain.model.Room;
import com.meetingrooms.domain.model.port.RoomQueryPort;
import com.meetingrooms.shared.ErrorMessages;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListRoomsHandler {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;

    private final RoomQueryPort roomQueryPort;

    public ListRoomsHandler(RoomQueryPort roomQueryPort) {
        this.roomQueryPort = roomQueryPort;
    }

    @Transactional(readOnly = true)
    public ListRoomsResult handle(ListRoomsQuery query) {
        int page = query.page() == null ? DEFAULT_PAGE : query.page();
        int size = query.size() == null ? DEFAULT_SIZE : query.size();
        if (page < 0 || size < 1) {
            throw new ValidationException(ErrorMessages.INVALID_PAGE_OR_SIZE);
        }
        List<Room> items = roomQueryPort.findActive(page, size);
        long total = roomQueryPort.countActive();
        return new ListRoomsResult(items, page, size, total);
    }
}
