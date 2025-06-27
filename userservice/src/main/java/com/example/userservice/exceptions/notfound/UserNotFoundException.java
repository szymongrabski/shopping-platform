package com.example.userservice.exceptions.notfound;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(Long id) {
        super("User with id " + id + " not found");
    }
}
