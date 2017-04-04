package com.isf;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

// SLF4J doesn't support lambdas (yet)
// https://github.com/qos-ch/slf4j/pull/70
// to make this example work, we need to use log4j2 directly
@Log4j2
@RestController
public class MovieController {

    private RestTemplate restTemplate;

    public MovieController(){
        this.restTemplate = new RestTemplate();
    }

    @GetMapping(value = "movie/{term}")
    public Movie getMovies(@PathVariable String term){
        Movie movie = Movie.from(restTemplate.getForObject("http://www.omdbapi.com/?t=" + term, Map.class));
        log.debug("got back movies: {}", movie);

        // if a method used in the logger is slow....
        log.debug("not using a lambda: '{}'", someComplexMethod());
        log.debug("using a lambda: '{}'", () -> someComplexMethod());
        return movie;
    }

    private String someComplexMethod() {
        System.out.println("DOING SOMETHING LONG AND INTENSIVE");
        return "a difficult to calculate string";
    }
}
