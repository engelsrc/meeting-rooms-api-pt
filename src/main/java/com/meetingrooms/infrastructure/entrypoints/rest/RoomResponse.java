package com.meetingrooms.infrastructure.entrypoints.rest;

public record RoomResponse(Long id, String name, int capacity, String location) {
}
