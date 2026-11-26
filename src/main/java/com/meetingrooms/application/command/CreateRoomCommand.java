package com.meetingrooms.application.command;

public record CreateRoomCommand(String name, int capacity, String location) {
}
