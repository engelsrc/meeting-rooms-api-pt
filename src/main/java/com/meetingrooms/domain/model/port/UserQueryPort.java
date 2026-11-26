package com.meetingrooms.domain.model.port;

import com.meetingrooms.domain.model.User;

import java.util.Optional;

public interface UserQueryPort {

    Optional<User> findByUsername(String username);
}
