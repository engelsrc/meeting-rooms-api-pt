package com.meetingrooms.infrastructure.adapters.persistence;

import com.meetingrooms.domain.model.User;
import com.meetingrooms.domain.model.port.UserQueryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserPersistenceAdapter implements UserQueryPort {

    private final UserJpaRepository repository;

    public UserPersistenceAdapter(UserJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username).map(this::toDomain);
    }

    private User toDomain(UserJpaEntity entity) {
        User user = new User(entity.getUsername(), entity.getPassword());
        user.setId(entity.getId());
        return user;
    }
}
