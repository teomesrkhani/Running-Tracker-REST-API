package com.teomesrkhani.running_tracker.run;

import java.time.LocalDateTime;

public record Run(
        Integer id,
        String title,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer kilometers,
        Location location)
{}
