package com.isf;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.slf4j.MarkerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@Slf4j
public class MovieController {

    private RestTemplate restTemplate;

    public MovieController(){
        this.restTemplate = new RestTemplate();
    }

    @GetMapping(value = "movie/{term}")
    public Movie getMovies(@PathVariable String term){
        // assume these values were put into the MDC when the user authenticated
        MDC.put("user.login", "jberk");
        MDC.put("user.name", "Jason Berk");

        Movie movie = Movie.from(restTemplate.getForObject("http://www.omdbapi.com/?t=" + term, Map.class));
        log.info("got back movies: {}", movie);
        if(movie.getRating().equalsIgnoreCase("R")) {
            log.warn(MarkerFactory.getMarker("RATED_R"), "{}", movie);
        }
        MDC.clear();
        return movie;
    }
}
