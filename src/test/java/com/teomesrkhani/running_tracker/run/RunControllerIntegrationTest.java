package com.teomesrkhani.running_tracker.run;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import java.time.LocalDateTime;

import java.util.List;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RunControllerIntegrationTest {

    @LocalServerPort
    int randomServerPort;

    RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create("http://localhost:" + randomServerPort);
    }

    @Test
    void shouldFindAllRuns() {
        List<Run> runs = restClient.get()
                .uri("/api/runs")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        assertEquals(10, runs.size());
    }

    @Test
    void shouldFindRunById() {
        Run run = restClient.get()
                .uri("/api/runs/2") // Changed the ID to 2
                .retrieve()
                .body(Run.class);

        assertAll(
                () -> assertEquals(2, run.id()),
                () -> assertEquals("Afternoon Run", run.title()), // Changed the title
                () -> assertEquals("2024-02-22T12:16", run.startTime().toString()), // Changed the date and time
                () -> assertEquals("2024-02-22T14:27", run.endTime().toString()), // Changed the date and time
                () -> assertEquals(6, run.kilometers()), // Changed the miles
                () -> assertEquals(Location.OUTDOOR, run.location())); // Changed the location
    }

    @Test
    void shouldCreateNewRun() {
        Run run = new Run(12, "Afternoon Run", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(3), 8, Location.INDOOR); // Changed values

        ResponseEntity<Void> newRun = restClient.post()
                .uri("/api/runs")
                .body(run)
                .retrieve()
                .toBodilessEntity();

        assertEquals(201, newRun.getStatusCodeValue());
    }

    @Test
    void shouldUpdateExistingRun() {
        Run run = restClient.get().uri("/api/runs/2").retrieve().body(Run.class); // Changed the ID to 2

        Run updatedRunDetails = new Run(run.id(), "Updated Morning Run", run.startTime(), run.endTime(), run.kilometers() + 1, run.location()); // Updated details
        ResponseEntity<Void> updatedRun = restClient.put()
                .uri("/api/runs/2") // Changed the ID to 2
                .body(updatedRunDetails)
                .retrieve()
                .toBodilessEntity();

        assertEquals(204, updatedRun.getStatusCodeValue());
    }

    @Test
    void shouldDeleteRun() {
        ResponseEntity<Void> run = restClient.delete()
                .uri("/api/runs/3") // Changed the ID to 3
                .retrieve()
                .toBodilessEntity();

        assertEquals(204, run.getStatusCodeValue());
    }
}