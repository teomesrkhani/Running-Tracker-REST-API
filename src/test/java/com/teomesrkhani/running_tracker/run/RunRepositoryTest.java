package com.teomesrkhani.running_tracker.run;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(RunRepository.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Use the actual database
class RunRepositoryTest {

    @Autowired
    RunRepository runRepository;

    @BeforeEach
    void setUp() {
        runRepository.create(new Run(1,
                "Monday Morning Run",
                LocalDateTime.now(),
                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
                3,
                Location.INDOOR));

        runRepository.create(new Run(2,
                "Wednesday Evening Run",
                LocalDateTime.now(),
                LocalDateTime.now().plus(60, ChronoUnit.MINUTES),
                6,
                Location.INDOOR));
    }

    @Test
    void shouldFindAllRuns() {
        List<Run> runs = runRepository.findAll();
        assertEquals(2, runs.size());
    }

    @Test
    void shouldFindRunWithValidId() {
        var run = runRepository.findById(1).get();
        assertEquals("Monday Morning Run", run.title());
        assertEquals(3, run.kilometers());
    }

    @Test
    void shouldNotFindRunWithInvalidId() {
        var run = runRepository.findById(3);
        assertTrue(run.isEmpty());
    }

    @Test
    void shouldCreateNewRun() {
        runRepository.create(new Run(3,
                "Friday Morning Run",
                LocalDateTime.now(),
                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
                3,
                Location.INDOOR));
        List<Run> runs = runRepository.findAll();
        assertEquals(3, runs.size());
    }

    @Test
    void shouldUpdateRun() {
        runRepository.update(new Run(1,
                "Monday Morning Run",
                LocalDateTime.now(),
                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
                5,
                Location.OUTDOOR), 1);
        var run = runRepository.findById(1).get();
        assertEquals("Monday Morning Run", run.title());
        assertEquals(5, run.kilometers());
        assertEquals(Location.OUTDOOR, run.location());
    }

    @Test
    void shouldDeleteRun() {
        runRepository.delete(1);
        List<Run> runs = runRepository.findAll();
        assertEquals(1, runs.size());
    }

    @Test
    void shouldNotDeleteRunWithInvalidId() {
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            runRepository.delete(3);
        });

        assertEquals("Failed to delete run 3", thrown.getMessage());

        List<Run> runs = runRepository.findAll();
        assertEquals(2, runs.size());
    }

    @Test
    void shouldHandleEmptyDatabase() {
        runRepository.delete(1);
        runRepository.delete(2);
        List<Run> runs = runRepository.findAll();
        assertTrue(runs.isEmpty());
    }

    @Test
    void shouldCreateRunWithEdgeCaseTitle() {
        runRepository.create(new Run(4,
                "",
                LocalDateTime.now(),
                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
                3,
                Location.OUTDOOR));
        var run = runRepository.findById(4).get();
        assertEquals("", run.title());
        assertEquals(3, run.kilometers());
    }

    @Test
    void shouldNotUpdateNonExistentRun() {
        Run run = new Run(999,
                "Non-existent Run",
                LocalDateTime.now(),
                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
                3,
                Location.INDOOR);
        assertThrows(RuntimeException.class, () -> runRepository.update(run, 999));
    }

    @Test
    void shouldHandleConcurrentUpdates() {
        Run run1 = new Run(1,
                "Concurrent Update Run 1",
                LocalDateTime.now(),
                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
                5,
                Location.OUTDOOR);
        Run run2 = new Run(1,
                "Concurrent Update Run 2",
                LocalDateTime.now(),
                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
                10,
                Location.INDOOR);
        runRepository.update(run1, 1);
        runRepository.update(run2, 1);
        var run = runRepository.findById(1).get();
        assertEquals("Concurrent Update Run 2", run.title());
        assertEquals(10, run.kilometers());
    }

}