package com.imjustdoom.flappyanticheat.util;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

public final class PlayerUtil {

    /**
     * Get the players ping
     * @param player - Player to get the ping from
     * @return - Returns the players ping
     */
    public static int getPing(final Player player) {
        try {
            final Method handle = player.getClass().getMethod("getHandle");
            final Object nmsHandle = handle.invoke(player);
            final Field pingField = nmsHandle.getClass().getField("ping");
            return pingField.getInt(nmsHandle);
        } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException
                | NoSuchFieldException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Exception while trying to get player ping.", e);
        }

        return -1;
    }

    /**
     * Checks if the player is on a climbable block
     * @param player - Player to check if they are on a climbable
     * @return - Returns if the player is on a climbable
     */
    public static boolean isOnClimbable(final Player player) {
        return player.getLocation().getBlock().getType() == XMaterial.LADDER.parseMaterial()
                || player.getLocation().getBlock().getType() == XMaterial.VINE.parseMaterial();
    }

    /**
     * Get all entities within a certain radius
     * From AntiHaxerMan
     * @param location - Location to get entities within the radius from
     * @param radius - Radius to check
     * @return - Returns the entities within the certain radius
     */
    public static List<Entity> getEntitiesWithinRadius(final Location location, final double radius) {
        try {
            final double expander = 16.0D;

            final double x = location.getX();
            final double z = location.getZ();

            final int minX = (int) Math.floor((x - radius) / expander);
            final int maxX = (int) Math.floor((x + radius) / expander);

            final int minZ = (int) Math.floor((z - radius) / expander);
            final int maxZ = (int) Math.floor((z + radius) / expander);

            final World world = location.getWorld();

            final List<Entity> entities = new LinkedList<>();

            for (int xVal = minX; xVal <= maxX; xVal++) {

                for (int zVal = minZ; zVal <= maxZ; zVal++) {

                    if (!world.isChunkLoaded(xVal, zVal)) continue;

                    for (final Entity entity : world.getChunkAt(xVal, zVal).getEntities()) {
                        //We have to do this due to stupidness
                        if (entity == null) break;

                        //Make sure the entity is within the radius specified
                        if (entity.getLocation().distanceSquared(location) > radius * radius) continue;

                        entities.add(entity);
                    }
                }
            }

            return entities;
        } catch (final Throwable ignored) {
        }

        return null;
    }

    /**
     * Get the players potion level
     * @param player - Player to check the potion effect from
     * @param effect - The effect to check the level of
     * @return - Returns the level of the effect
     */
    public int getPotionLevel(final Player player, final PotionEffectType effect) {
        final int effectId = effect.getId();

        if (!player.hasPotionEffect(effect)) return 0;

        for (final PotionEffect potionEffect : player.getActivePotionEffects()) {
            if (potionEffect.getType().getId() == effectId) {
                return potionEffect.getAmplifier() + 1;
            }
        }

        return 0;
    }
}