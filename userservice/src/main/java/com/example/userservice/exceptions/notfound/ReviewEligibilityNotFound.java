package com.example.userservice.exceptions.notfound;

public class ReviewEligibilityNotFound extends NotFoundException {
    public ReviewEligibilityNotFound(Long reviewerId, Long revieweeId) {
        super("User " + reviewerId + " is not eligible to review " + revieweeId);
    }
}
