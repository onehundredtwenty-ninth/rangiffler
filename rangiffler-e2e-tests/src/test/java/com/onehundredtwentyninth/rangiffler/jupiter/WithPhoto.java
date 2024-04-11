package com.onehundredtwentyninth.rangiffler.jupiter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface WithPhoto {

  String countryCode() default "us";

  String image() default "";

  String description() default "";
}
