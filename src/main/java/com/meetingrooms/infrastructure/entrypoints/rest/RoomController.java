package com.meetingrooms.infrastructure.entrypoints.rest;

import com.meetingrooms.application.ValidationException;
import com.meetingrooms.application.command.CreateRoomCommand;
import com.meetingrooms.application.command.CreateRoomHandler;
import com.meetingrooms.application.query.ListRoomsHandler;
import com.meetingrooms.application.query.ListRoomsQuery;
import com.meetingrooms.application.query.ListRoomsResult;
import com.meetingrooms.domain.model.Room;
import com.meetingrooms.infrastructure.config.OpenApiConfig;
import com.meetingrooms.shared.ErrorMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/rooms")
@Tag(name = "Rooms")
public class RoomController {

    private final CreateRoomHandler createRoomHandler;
    private final ListRoomsHandler listRoomsHandler;

    public RoomController(CreateRoomHandler createRoomHandler, ListRoomsHandler listRoomsHandler) {
        this.createRoomHandler = createRoomHandler;
        this.listRoomsHandler = listRoomsHandler;
    }

    @PostMapping
    @Operation(summary = "Create room")
    @SecurityRequirement(name = OpenApiConfig.BEARER_JWT_SCHEME)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "403", description = "Unauthenticated"),
            @ApiResponse(responseCode = "409", description = "Room name already exists")
    })
    public ResponseEntity<RoomResponse> create(@RequestBody CreateRoomRequest request) {
        if (request == null || request.capacity() == null) {
            throw new ValidationException(ErrorMessages.ROOM_CAPACITY_INVALID);
        }
        Room room = createRoomHandler.handle(
                new CreateRoomCommand(
                        request.name(),
                        request.capacity(),
                        request.location(),
                        CurrentUser.requireUsername()));
        return ResponseEntity.status(HttpStatus.CREATED).body(RoomMapper.toResponse(room));
    }

    @GetMapping
    @Operation(summary = "List rooms")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    public PageResponse<RoomResponse> list(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        ListRoomsResult result = listRoomsHandler.handle(new ListRoomsQuery(page, size));
        List<RoomResponse> items = result.items().stream().map(RoomMapper::toResponse).toList();
        return new PageResponse<>(items, result.page(), result.size(), result.total());
    }
}
