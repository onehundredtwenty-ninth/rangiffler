package com.onehundredtwentyninth.rangiffler.jupiter.annotation;

import com.onehundredtwentyninth.rangiffler.model.CountryCodes;
import com.onehundredtwentyninth.rangiffler.model.UserAvatars;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CreateUser {

  String username() default "";

  String password() default "";

  CountryCodes countryCode() default CountryCodes.US;

  UserAvatars avatar() default UserAvatars.BEE;

  Friend[] friends() default {};

  WithPhoto[] photos() default {};
}
