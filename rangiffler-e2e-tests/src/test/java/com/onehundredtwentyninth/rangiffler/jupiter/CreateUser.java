package com.onehundredtwentyninth.rangiffler.jupiter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CreateUser {

  String username() default "";

  String password() default "";

  boolean handle() default true;
}