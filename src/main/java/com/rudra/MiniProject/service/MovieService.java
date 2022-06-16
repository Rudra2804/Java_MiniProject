package com.rudra.MiniProject.service;

import com.rudra.MiniProject.controller.MovieContrller;
import com.rudra.MiniProject.entity.Movie;
import com.rudra.MiniProject.exceptions.MovieNotFoundException;
import com.rudra.MiniProject.repository.MovieRepo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class MovieService {
    @Autowired
    private MovieRepo movierepo;

    private static final Logger log = LoggerFactory.getLogger(MovieService.class);

    public List<Movie> saveMovie() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/Movies.csv"));
            while (br.readLine() != null) {
                String[] line = br.readLine().split(",");
                System.out.println("line ="+br.readLine());
                Movie m = new Movie();
                m.setImdb_title_id(line[0]);
                m.setTitle(line[1]);
                m.setOriginal_title(line[2]);
                m.setS_year(line[3]);
                m.setDate_published(line[4]);
                m.setGenre(line[5]);
                m.setDuration(Integer.parseInt(line[6]));
                m.setCountry(line[7]);
                m.setLanguage(line[8]);
                m.setDirector(line[9]);
                m.setWriter(line[10]);
                m.setProduction_company(line[11]);
                m.setActors(line[12]);
                m.setDescription(line[13]);
                m.setAvg_vote(Double.parseDouble(line[14]));
                m.setVotes(Integer.parseInt(line[15]));
                m.setBudget(line[16]);
                m.setUsa_gross_income(line[17]);
                m.setWorlwide_gross_income(line[18]);
                m.setMetascore(line[19]);
                m.setReviews_from_users(Integer.parseInt(line[20]));
                m.setReviews_from_critics(Integer.parseInt(line[21]));

                movierepo.save(m);

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return movierepo.loadData();
    }

    public List<Movie> getDataByDirectorNameAndYear(String directorName, String startYear, String endYear) {
        return movierepo.fetchDatabyDirectorAndYearRange(directorName,startYear,endYear);
    }

    public List<Movie> findByUserReviewsGreaterThanGiven(Integer userReview) {
        return movierepo.findByUserReviewsGreaterThanGiven(userReview);
    }

    public Integer findByYearAndCountries(String year, String country) {
        return movierepo.findByYearAndCountries(year,country);
    }

    public void addMovie(Movie m) {
        movierepo.save(m);
    }

    public ResponseEntity<?> findById(String id,String imdb_title_id) {
        log.info("Fetch Movie using ID " + this.getClass().getName());
        try {
            Movie r = movierepo.findById(id,imdb_title_id);
            if (r == null) {
                throw new MovieNotFoundException("No Movie  found");
            }
            return new ResponseEntity<>(r, HttpStatus.OK);
        } catch (MovieNotFoundException e) {
            return new ResponseEntity<>("No Movie Rating found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> deleteById(String id,String imdb_id) {
        log.info("Delete Movie using ID " + this.getClass().getName());
        try {
            Movie r = movierepo.deleteById(id,imdb_id);
           return new ResponseEntity<>(r,HttpStatus.OK);
        } catch (MovieNotFoundException e) {
            return new ResponseEntity<>("No Movie Rating found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
