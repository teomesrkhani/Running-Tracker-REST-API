package com.teomesrkhani.running_tracker.run;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // need to add this annotation to respond to HTTP requests and return response in JSON
@RequestMapping("/api/runs")
public class RunController {

    private final RunRepository runRepository;

    // Dependency Injection
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
        throw new RunNotFoundException();
    }

    // Create (Post)
    @ResponseStatus(HttpStatus.CREATED) // return 201 status code
    @PostMapping("")
    void create(@Valid @RequestBody Run run){
        runRepository.create(run);
    }

    // Update (Put)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void update(@RequestBody Run run, @PathVariable Integer id){
        runRepository.update(run, id);
    }

    // Delete (Delete)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id){
        runRepository.delete(id);
    }

}
