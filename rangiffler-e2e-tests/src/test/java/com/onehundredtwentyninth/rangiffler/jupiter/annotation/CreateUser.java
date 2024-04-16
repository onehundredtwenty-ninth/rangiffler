package com.onehundredtwentyninth.rangiffler.jupiter.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CreateUser {

  String username() default "";

  String password() default "";

  Friend[] friends() default {};

  WithPhoto[] photos() default {};
}
