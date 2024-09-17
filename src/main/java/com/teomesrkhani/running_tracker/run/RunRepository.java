package com.teomesrkhani.running_tracker.run;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RunRepository {
    private List<Run> runs = new ArrayList<>();

    List<Run> findAll() {
        return runs;
    }

    Optional<Run> findById(Integer id){
        return runs.stream()
                .filter(run -> run.id().equals(id))
                .findFirst();
    }

    // Create (Post)
    void create(Run run) {
        runs.add(run);
    }

    @PostConstruct
    private void init() {
        runs.add(new Run(1,
                "Morning Run",
                LocalDateTime.now(),
                LocalDateTime.now().plus(45, ChronoUnit.MINUTES),
                5, Location.INDOOR));

        runs.add(new Run(2,
                "Evening Run",
                LocalDateTime.now(),
                LocalDateTime.now().plus(25, ChronoUnit.MINUTES),
                3,
                Location.OUTDOOR));
    }
}
