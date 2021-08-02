package com.justdoom.flappyanticheat.checks;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CheckInfo {
    String check();
    String checkType();
    boolean experimental();
    String description();
}