package com.rudra.MiniProject.controller;

import com.rudra.MiniProject.exceptions.MovieNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = MovieNotFoundException.class)
    public ResponseEntity<Object> exception(MovieNotFoundException exception) {
        return new ResponseEntity<>("Movie Rating not found", HttpStatus.NOT_FOUND);
    }

}
