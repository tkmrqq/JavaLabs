package com.find.movie.anime.exception;

public class AnimeNotFoundException extends RuntimeException{
    public AnimeNotFoundException(String message) {
        super(message);
    }
}
