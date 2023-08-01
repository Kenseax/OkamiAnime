package ru.anime.okami.exception;

import org.springframework.http.HttpStatus;

public class AnimeAPIException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public AnimeAPIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public AnimeAPIException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
