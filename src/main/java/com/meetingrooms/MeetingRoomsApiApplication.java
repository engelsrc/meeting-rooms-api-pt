package com.meetingrooms;

import com.meetingrooms.infrastructure.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class MeetingRoomsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeetingRoomsApiApplication.class, args);
    }
}
