package com.meetingrooms.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetingrooms.infrastructure.entrypoints.rest.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsonAuthErrorWriter {

    private final ObjectMapper objectMapper;

    public JsonAuthErrorWriter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void write(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), new ErrorResponse(message));
    }
}
