package com.imjustdoom.flappyanticheat.util;

import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import com.imjustdoom.flappyanticheat.FlappyAnticheatPlugin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.LivingEntityMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import net.minestom.server.potion.PotionEffect;

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
            System.out.println("Exception while trying to get player ping.");
        }

        return -1;
    }

    /**
     * Get all entities within a certain radius
     * From AntiHaxerMan
     * @param location - Location to get entities within the radius from
     * @param radius - Radius to check
     * @return - Returns the entities within the certain radius
     */
    public static List<Entity> getEntitiesWithinRadius(final Pos location, final double radius, final Instance instance) {
        try {
            final double expander = 16.0D;

            final double x = location.x();
            final double z = location.z();

            final int minX = (int) Math.floor((x - radius) / expander);
            final int maxX = (int) Math.floor((x + radius) / expander);

            final int minZ = (int) Math.floor((z - radius) / expander);
            final int maxZ = (int) Math.floor((z + radius) / expander);

            final List<Entity> entities = new LinkedList<>();

            for (int xVal = minX; xVal <= maxX; xVal++) {

                for (int zVal = minZ; zVal <= maxZ; zVal++) {

                    if (!instance.isChunkLoaded(xVal, zVal)) continue;

                    // TODO: getEntities in a chunk

                    /**for (final Entity entity : instance.getChunkAt(xVal, zVal).getEntities()) {
                        //We have to do this due to stupidness
                        if (entity == null) break;

                        //Make sure the entity is within the radius specified
                        if (entity.getPosition().distanceSquared(location) > radius * radius) continue;

                        entities.add(entity);
                    }**/
                }
            }

            return entities;
        } catch (final Throwable ignored) {
        }

        return null;
    }

    // TODO: potion effects

    /**
     * Get the players potion level
     * @param player - Player to check the potion effect from
     * @param effect - The effect to check the level of
     * @return - Returns the level of the effect
     */
    /**public int getPotionLevel(final Player player, final PotionEffect effect) {
        final int effectId = effect.id();

        if (!player.hasPotionEffect(effect)) return 0;

        for (final PotionEffect potionEffect : player.getActivePotionEffects()) {
            if (potionEffect.getType().getId() == effectId) {
                return potionEffect.getAmplifier() + 1;
            }
        }

        return 0;
    }**/

    // Credit to https://github.com/Bloepiloepi/MinestomPvP
    public static boolean isBlocking(LivingEntity entity) {
        LivingEntityMeta meta = (LivingEntityMeta) entity.getEntityMeta();

        if (meta.isHandActive()) {
            return entity.getItemInHand(meta.getActiveHand()).getMaterial() == Material.SHIELD;
        }

        return false;
    }
}