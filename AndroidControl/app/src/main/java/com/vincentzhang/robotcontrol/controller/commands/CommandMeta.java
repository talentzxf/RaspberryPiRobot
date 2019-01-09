package com.vincentzhang.robotcontrol.controller.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by VincentZhang on 1/9/2019.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommandMeta {
    String tag() default "undef";
    String pattern() default "undef";
}
