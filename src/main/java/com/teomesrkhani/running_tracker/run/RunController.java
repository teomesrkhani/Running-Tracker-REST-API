package com.teomesrkhani.running_tracker.run;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController // need to add this annotation to respond to HTTP requests and return response in JSON
@RequestMapping("/api/runs")
public class RunController {

    private final RunRepository runRepository;

    public RunController(RunRepository runRepository){
        this.runRepository = runRepository;
    }

    @GetMapping("")
    List<Run> findAll() {
        return runRepository.findAll();
    }
    @GetMapping("/{id}")
    Run findById(@PathVariable Integer id) {
        Optional<Run> run = runRepository.findById(id);
        if(run.isPresent()){
            return run.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Run not found");
    }

    // Create (Post)
    @ResponseStatus(HttpStatus.CREATED) // return 201 status code
    @PostMapping
    void create(@RequestBody Run run){
        runRepository.create(run);
    }

    // Update (Put)

    // Delete (Delete)


}
