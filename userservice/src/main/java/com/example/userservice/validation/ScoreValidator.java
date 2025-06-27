package com.example.userservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ScoreValidator implements ConstraintValidator<ValidScore, Double> {
    @Override
    public boolean isValid(Double score, ConstraintValidatorContext context) {
        if (score == null) {
            return false;
        }
        if (score < 1.0 || score > 5.0) {
            return false;
        }
        double fractionalPart = score - Math.floor(score);
        return fractionalPart == 0.0 || fractionalPart == 0.5;
    }
}