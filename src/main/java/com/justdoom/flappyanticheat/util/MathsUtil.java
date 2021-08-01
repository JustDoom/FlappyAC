package com.justdoom.flappyanticheat.util;

public class MathsUtil {

    public static float roundNumber(double n) {
        return (float) (Math.round(n * 10000.0) / 10000.0);
    }
}