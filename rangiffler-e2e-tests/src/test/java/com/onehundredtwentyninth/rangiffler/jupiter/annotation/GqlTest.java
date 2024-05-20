package com.onehundredtwentyninth.rangiffler.jupiter.annotation;

import com.onehundredtwentyninth.rangiffler.jupiter.extension.ApiLoginExtension;
import com.onehundredtwentyninth.rangiffler.jupiter.extension.CreateExtrasUserExtension;
import com.onehundredtwentyninth.rangiffler.jupiter.extension.CreateUserExtension;
import com.onehundredtwentyninth.rangiffler.jupiter.extension.GuiceExtension;
import io.qameta.allure.junit5.AllureJunit5;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith({GuiceExtension.class, AllureJunit5.class, CreateUserExtension.class, CreateExtrasUserExtension.class,
    ApiLoginExtension.class})
public @interface GqlTest {

}
