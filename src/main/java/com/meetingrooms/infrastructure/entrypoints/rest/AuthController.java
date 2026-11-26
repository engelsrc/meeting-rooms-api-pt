package com.meetingrooms.infrastructure.entrypoints.rest;

import com.meetingrooms.application.ValidationException;
import com.meetingrooms.application.command.LoginCommand;
import com.meetingrooms.application.command.LoginHandler;
import com.meetingrooms.shared.ErrorMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth")
public class AuthController {

    private final LoginHandler loginHandler;

    public AuthController(LoginHandler loginHandler) {
        this.loginHandler = loginHandler;
    }

    @PostMapping("/login")
    @Operation(summary = "Login and obtain JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authenticated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public LoginResponse login(@RequestBody LoginRequest request) {
        if (request == null) {
            throw new ValidationException(ErrorMessages.VALIDATION_FAILED);
        }
        String token = loginHandler.handle(new LoginCommand(request.username(), request.password()));
        return new LoginResponse(token);
    }
}
