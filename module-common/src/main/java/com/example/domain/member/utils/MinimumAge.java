package com.example.domain.member.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {MinimumAgeValidator.class})
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MinimumAge {

    String message() default "만 14세 이상 가입이 가능합니다";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

    String regexp() default ".*";
    String pattern() default "yyyy-MM-dd";
}
