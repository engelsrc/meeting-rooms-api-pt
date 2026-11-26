package com.meetingrooms.infrastructure.entrypoints.rest;

public record CreateRoomRequest(String name, Integer capacity, String location) {
}
