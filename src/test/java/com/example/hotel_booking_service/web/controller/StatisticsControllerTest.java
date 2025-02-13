package com.example.hotel_booking_service.web.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

//    @Autowired
//    private StatisticsService statisticsService;

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @BeforeEach
    void setUp() {
        mongoDBContainer.start();
        System.setProperty("spring.data.mongodb.uri", mongoDBContainer.getReplicaSetUrl());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldExportStatisticsAsCsv() throws Exception {
        mockMvc.perform(get("/rest/admin-ui/statistics/export")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=statistics.csv"));
    }

    @Test
    void shouldReturnUnauthorizedForNonAdmin() throws Exception {
        mockMvc.perform(get("/rest/admin-ui/statistics/export"))
                .andExpect(status().isUnauthorized());
    }
}
