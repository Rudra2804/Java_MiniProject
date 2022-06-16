package com.rudra.MiniProject.controller;

import com.rudra.MiniProject.entity.Movie;
import com.rudra.MiniProject.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class MovieContrller {
    @Autowired
    private MovieService service;
    private static final Logger log = LoggerFactory.getLogger(MovieContrller.class);

    @RequestMapping("/feedDataFromCSV")
    public List<Movie> saveData() {
        log.debug("start reading data from csv file");
        return service.saveMovie();
    }

    // Ans of 1. Titles directed by given director in the given year range
    // e.g : generate titles report for director D.W. Griffith and year range 2010 to 2020
    @PostMapping("/findByDirectorNameAndYear")
    public List<Movie> getDataByDirectorNameAndYear(@RequestParam String directorName, @RequestParam String startYear, @RequestParam String endYear) {
        log.debug("Inside in the getDataByDirectorNameAndYear() ");
        return service.getDataByDirectorNameAndYear(directorName, startYear, endYear);
    }

    // Ans of 2.Generate report of English titles which have user reviews more than
    // given user review filter and sort the report with user reviews by descending
    @PostMapping("/findByUserReviewsGreaterThanGiven")
    public List<Movie> findByUserReviewsGreaterThanGiven(@RequestParam Integer userReview) {
        log.debug("Inside in the findByUserReviewsGreaterThanGiven() ");
        return service.findByUserReviewsGreaterThanGiven(userReview);
    }

    //Ans of 3.Generate highest budget titles for the given year and country filters
    @PostMapping("/findByYearAndCountries")
    public Integer findByYearAndCountries(@RequestParam String year,@RequestParam String country) {
        log.debug("Inside in the findByYearAndCountries() ");
        return service.findByYearAndCountries(year,country);
    }

    //Ans 4:create a TimingMiddleware that prints time taken
    // to execute each request.
    @PostMapping("/timingMiddleware")
    public Long timingMiddleware(@RequestParam Integer userReview) {
        log.debug("Inside in the timingMiddleware() ");
        Long starttime=System.currentTimeMillis();
        List<Movie> list=service.findByUserReviewsGreaterThanGiven(userReview);
        Long endtime=System.currentTimeMillis();
        log.debug("Completed timingMiddleware() ");
        return (endtime - starttime)/1000;
    }

    //Add New Movie
    @PostMapping("/addNewMovie")
    public void addNewMovie(@RequestBody Movie m) {
        log.debug("Inside in the addNewMovie() ");
         service.addMovie(m);
        log.debug("Completed addNewMovie() ");
    }

    //Find Movie By ID
    @GetMapping("/movies/{id}/{imdb_id}")
    public ResponseEntity<?> findMovieById(@PathVariable("id") String id,@PathVariable("imdb_id") String imdb_id){
        log.info("Inside findMovieById Method");
        return service.findById(id,imdb_id);
    }

    //Delete By Id
    @DeleteMapping("/movie/{id}/{imdb_id}")
    public ResponseEntity<?> deleteMoviesById(@PathVariable("id") String id,@PathVariable("imdb_id") String imdb_id){
        log.info("Inside deleteMoviesById Method");
        return service.deleteById(id,imdb_id);
    }


}
