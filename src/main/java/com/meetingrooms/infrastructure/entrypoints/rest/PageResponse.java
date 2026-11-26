package com.meetingrooms.infrastructure.entrypoints.rest;

import java.util.List;

public record PageResponse<T>(List<T> items, int page, int size, long total) {
}
