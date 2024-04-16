package com.onehundredtwentyninth.rangiffler.jupiter.annotation;

import com.onehundredtwentyninth.rangiffler.jupiter.converter.GqlRequestConverter;
import com.onehundredtwentyninth.rangiffler.jupiter.extension.GqlRequestResolver;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.converter.ConvertWith;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ExtendWith(GqlRequestResolver.class)
@ConvertWith(GqlRequestConverter.class)
public @interface GqlRequestFile {

  String value() default "";
}