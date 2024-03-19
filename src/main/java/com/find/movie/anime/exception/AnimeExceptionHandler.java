package com.find.movie.anime.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AnimeExceptionHandler {

    @ExceptionHandler(Exception.class) // Обработка 500 ошибок
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }

    @ExceptionHandler(AnimeNotFoundException.class) // Обработка 404 ошибок
    public ResponseEntity<String> handleAnimeNotFoundException(AnimeNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found: " + e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class) // Обработка 400 ошибок
    public ResponseEntity<String> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request: " + e.getMessage());
    }
}
