package com.imjustdoom.flappyanticheat.manager;

import com.imjustdoom.api.check.FlappyCheck;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.checks.combat.aura.AuraA;
import com.imjustdoom.flappyanticheat.checks.combat.criticals.CrititcalsA;
import com.imjustdoom.flappyanticheat.checks.movement.boatfly.BoatFlyA;
import com.imjustdoom.flappyanticheat.checks.movement.fly.FlyA;
import com.imjustdoom.flappyanticheat.checks.movement.fly.FlyB;
import com.imjustdoom.flappyanticheat.checks.movement.fly.FlyC;
import com.imjustdoom.flappyanticheat.checks.movement.nofall.NoFallA;
import com.imjustdoom.flappyanticheat.checks.movement.nofall.NoFallB;
import com.imjustdoom.flappyanticheat.checks.movement.noslow.NoSlowA;
import com.imjustdoom.flappyanticheat.checks.player.badpackets.BadPacketsA;
import com.imjustdoom.flappyanticheat.checks.player.badpackets.BadPacketsB;
import com.imjustdoom.flappyanticheat.checks.player.badpackets.BadPacketsC;
import com.imjustdoom.flappyanticheat.checks.player.inventory.InventoryA;
import com.imjustdoom.flappyanticheat.checks.player.inventory.InventoryB;
import com.imjustdoom.flappyanticheat.checks.player.inventory.InventoryC;
import com.imjustdoom.flappyanticheat.checks.player.scaffold.ScaffoldA;
import com.imjustdoom.flappyanticheat.checks.player.scaffold.ScaffoldB;
import com.imjustdoom.flappyanticheat.checks.player.skinblinker.SkinBlinkerA;
import com.imjustdoom.flappyanticheat.checks.player.timer.TimerA;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class CheckManager {

    public static final Class<?>[] CHECKS = new Class[]{
            //Movement
            NoFallA.class,
            NoFallB.class,
            FlyA.class,
            FlyB.class,
            ScaffoldA.class,
            ScaffoldB.class,
            //SpeedA.class,
            NoSlowA.class,
            FlyC.class,
            BoatFlyA.class,

            //Combat
            CrititcalsA.class,
            AuraA.class,

            //Player
            SkinBlinkerA.class,
            TimerA.class,
            BadPacketsA.class,
            BadPacketsB.class,
            BadPacketsC.class,
            InventoryA.class,
            InventoryB.class,
            InventoryC.class
    };

    public static final List<Constructor<?>> CONSTRUCTORS = new ArrayList<>();

    public static List<FlappyCheck> loadChecks(final FlappyPlayer player) {
        List<FlappyCheck> checkList = new ArrayList<>();
        for (Constructor<?> constructor : CONSTRUCTORS) {
            try {
                //System.out.println("check loaded");
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
