package com.meetingrooms.infrastructure.entrypoints.rest;

import com.meetingrooms.application.ValidationException;
import com.meetingrooms.application.command.LoginCommand;
import com.meetingrooms.application.command.LoginHandler;
import com.meetingrooms.shared.ErrorMessages;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final LoginHandler loginHandler;

    public AuthController(LoginHandler loginHandler) {
        this.loginHandler = loginHandler;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        if (request == null) {
            throw new ValidationException(ErrorMessages.VALIDATION_FAILED);
        }
        String token = loginHandler.handle(new LoginCommand(request.username(), request.password()));
        return new LoginResponse(token);
    }
}
