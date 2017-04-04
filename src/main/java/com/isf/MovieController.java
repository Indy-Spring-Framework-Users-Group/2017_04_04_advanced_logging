package com.isf;

import lombok.extern.slf4j.Slf4j;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.json.JSONObject;
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
        Movie movie = Movie.from(restTemplate.getForObject("http://www.omdbapi.com/?t=" + term, Map.class));
        log.debug("got back movies: {}", movie);

        saveAsWord(movie);

        return movie;
    }

    private void saveAsWord(Movie movie) {
        try {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
            MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();
            mdp.addParagraphOfText(new JSONObject(movie).toString(4));
            wordMLPackage.save(new java.io.File(movie.getTitle() + ".docx") );
        }catch (Exception e){
            log.error("failed to create word doc from movie {}", movie);
            throw new RuntimeException(e);
        }
    }
}
