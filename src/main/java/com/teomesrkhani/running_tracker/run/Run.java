package com.teomesrkhani.running_tracker.run;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record Run(
        Integer id,

        @NotEmpty
        String title,
        LocalDateTime startTime,
        LocalDateTime endTime,

        @Positive
        Integer kilometers,
        Location location
) {

    // Validate if user input is correct

    public Run {
        if(endTime.isBefore(startTime)){
            throw new IllegalArgumentException("Start time must be before end time");
        }
    }

}
