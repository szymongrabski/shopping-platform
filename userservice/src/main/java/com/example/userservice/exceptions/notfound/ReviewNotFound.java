package com.example.userservice.exceptions.notfound;

public class ReviewNotFound extends NotFoundException {
    public ReviewNotFound(Long id) {
        super("Review with id " + id + " not found");
    }
}
