package com.udacity.course3.reviews.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Comment not found.")
public class CommentsNotFoundException extends RuntimeException {
    public CommentsNotFoundException(){
    }

    public CommentsNotFoundException(String message){
        super(message);
    }
}
