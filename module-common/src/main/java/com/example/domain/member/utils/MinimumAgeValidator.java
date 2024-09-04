package com.example.domain.member.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class MinimumAgeValidator implements ConstraintValidator<MinimumAge, LocalDate> {


    @Override
    public void initialize(MinimumAge constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate birthday, ConstraintValidatorContext constraintValidatorContext) {

        LocalDate now = LocalDate.now();

        int americanAge = now.minusYears(birthday.getYear()).getYear(); //

        if (birthday.plusYears(americanAge).isAfter(now)) {
            americanAge = americanAge -1;
        }

        // 만 14세 이상
        if(americanAge >=14){
            return true;
        }
        // 만 14세 미만
        return false;
    }
}

