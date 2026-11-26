package com.meetingrooms.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetingrooms.support.AuthTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReservationOverlapIT {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void overlappingReservationReturns409() throws Exception {
        String token = AuthTestSupport.loginAdmin(mockMvc);
        String roomName = "IT-Overlap-" + UUID.randomUUID();

        String roomResponse = mockMvc.perform(post("/api/rooms")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + roomName + "\",\"capacity\":6}"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        long roomId = OBJECT_MAPPER.readTree(roomResponse).get("id").asLong();

        String firstBody = "{\"roomId\":" + roomId
                + ",\"startAt\":\"2026-12-15T10:00:00-06:00\""
                + ",\"endAt\":\"2026-12-15T11:00:00-06:00\"}";
        mockMvc.perform(post("/api/reservations")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(firstBody))
                .andExpect(status().isCreated());

        String overlapBody = "{\"roomId\":" + roomId
                + ",\"startAt\":\"2026-12-15T10:30:00-06:00\""
                + ",\"endAt\":\"2026-12-15T11:30:00-06:00\"}";
        mockMvc.perform(post("/api/reservations")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(overlapBody))
                .andExpect(status().isConflict());
    }
}
