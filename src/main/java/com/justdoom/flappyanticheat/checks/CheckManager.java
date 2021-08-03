package com.justdoom.flappyanticheat.checks;

import com.justdoom.flappyanticheat.checks.combat.criticals.CrititcalsA;
import com.justdoom.flappyanticheat.checks.movement.fly.FlyA;
import com.justdoom.flappyanticheat.checks.movement.nofall.NoFallA;
import com.justdoom.flappyanticheat.data.FlappyPlayer;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class CheckManager {

    public static final Class<?>[] CHECKS = new Class[]{
            NoFallA.class,
            FlyA.class,
            CrititcalsA.class
    };

    private static final List<Constructor<?>> CONSTRUCTORS = new ArrayList<>();

    public static List<Check> loadChecks(final FlappyPlayer player) {
        List<Check> checkList = new ArrayList<>();
        for (Constructor<?> constructor : CONSTRUCTORS) {
            try {
                System.out.println("check loaded");
                checkList.add((Check) constructor.newInstance(player));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return checkList;
    }

    public static void setup() {
        for (Class<?> clazz : CHECKS) {
            try {
                CONSTRUCTORS.add(clazz.getConstructor(FlappyPlayer.class));
            } catch (NoSuchMethodException exception) {
                exception.printStackTrace();
            }
        }
    }
}
