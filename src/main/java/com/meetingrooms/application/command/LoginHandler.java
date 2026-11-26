package com.meetingrooms.application.command;

import com.meetingrooms.application.UnauthorizedException;
import com.meetingrooms.application.ValidationException;
import com.meetingrooms.domain.model.User;
import com.meetingrooms.domain.model.port.UserQueryPort;
import com.meetingrooms.infrastructure.security.JwtService;
import com.meetingrooms.shared.ErrorMessages;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginHandler {

    private final UserQueryPort userQueryPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginHandler(
            UserQueryPort userQueryPort,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userQueryPort = userQueryPort;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional(readOnly = true)
    public String handle(LoginCommand command) {
        String username = command.username() == null ? null : command.username().trim();
        String password = command.password();
        if (username == null || username.isEmpty()) {
            throw new ValidationException(ErrorMessages.AUTH_USERNAME_REQUIRED);
        }
        if (password == null || password.isBlank()) {
            throw new ValidationException(ErrorMessages.AUTH_PASSWORD_REQUIRED);
        }

        User user = userQueryPort.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException(ErrorMessages.AUTH_USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException(ErrorMessages.AUTH_INVALID_PASSWORD);
        }
        return jwtService.createToken(user.getUsername());
    }
}
