package com.meetingrooms.infrastructure.entrypoints.rest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

final class CurrentUser {

    private CurrentUser() {
    }

    static String requireUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new IllegalStateException("Authenticated username is required");
        }
        return authentication.getName();
    }
}
