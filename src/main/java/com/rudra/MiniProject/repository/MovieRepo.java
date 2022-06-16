package com.rudra.MiniProject.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.rudra.MiniProject.entity.Movie;
import com.rudra.MiniProject.exceptions.MovieNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class MovieRepo {

    @Autowired
    public DynamoDBMapper dynamodbmapper;

    public Movie save(Movie movie) {
        dynamodbmapper.save(movie);
        return movie;
    }

    public List<Movie> loadData() {
        return dynamodbmapper.scan(Movie.class, new DynamoDBScanExpression());
    }

    public List<Movie> fetchDatabyDirectorAndYearRange(String directorName, String startYear, String endYear) {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":v1", new AttributeValue().withS(directorName));
        eav.put(":v2", new AttributeValue().withS(startYear));
        eav.put(":v3", new AttributeValue().withS(endYear));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("director = :v1 and s_year between :v2 and :v3")
                .withExpressionAttributeValues(eav);
        return dynamodbmapper.scan(Movie.class, scanExpression);

    }

    public List<Movie> findByUserReviewsGreaterThanGiven(Integer userReview) {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":v1", new AttributeValue().withN(userReview.toString()));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("reviews_from_users > :v1 ")
                .withExpressionAttributeValues(eav);
        return dynamodbmapper.scan(Movie.class, scanExpression);
    }

    public Integer findByYearAndCountries(String year, String country) {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":v1", new AttributeValue().withS(year));
        eav.put(":v2", new AttributeValue().withS(country));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("s_year = :v1 and country = :v2 ")
                .withExpressionAttributeValues(eav);
        List<Integer> budget=dynamodbmapper.scan(Movie.class, scanExpression).stream().map(x->Integer.parseInt(x.getBudget())).collect(Collectors.toList());
        return Collections.max(budget);

    }

    public Movie findById(String id, String imdb_title_id) {
        return dynamodbmapper.load(Movie.class,id,imdb_title_id);
    }

    public Movie deleteById(String id,String imdb_title_id) throws Exception {
        Movie m =dynamodbmapper.load(Movie.class,id,imdb_title_id);
        if(m == null){
            throw new MovieNotFoundException("No Movie Found");
        }
        dynamodbmapper.delete(m);
        return m;
    }
}
