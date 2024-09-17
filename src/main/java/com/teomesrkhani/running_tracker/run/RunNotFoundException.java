package com.teomesrkhani.running_tracker.run;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RunNotFoundException extends RuntimeException{
    public RunNotFoundException() {
        super("Run not found");
    }
}
