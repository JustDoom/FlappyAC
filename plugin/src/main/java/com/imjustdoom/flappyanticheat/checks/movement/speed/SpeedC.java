package com.imjustdoom.flappyanticheat.checks.movement.speed;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

@CheckInfo(check = "Speed", checkType = "C", experimental = false, description = "Checks players speed on the ground", type = CheckType.MOVEMENT)
public class SpeedC extends Check {

    public SpeedC(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {

        if (!packet.isFlying()
                || isExempt(ExemptType.JOINED, ExemptType.ENTITIES, ExemptType.FLYING/**ExemptType.WEB, ExemptType.TELEPORT**/)) return;

        float momentum = 0.91f * getFrictionBlock(data.getPlayer().getLocation().clone().add(0, -1.0, 0));
        float baseSpeed = getBaseSpeed(data.getPlayer(), data.getPlayer().isSprinting());
        float acceleration = (float) (baseSpeed * getEffectMultipliers(data.getPlayer()) * (0.16277f / Math.pow(momentum, 3)));
        float prediction = (float) ((data.getPositionProcessor().getLastDeltaXZ() * momentum) + acceleration); // Momentum + acceleration
        float accuracy = (float) ((data.getPositionProcessor().getDeltaXZ() - prediction) * 0.98f);

        /**debug("acc= " + accuracy);
        debug("pred= " + prediction);
        debug("accel= " + acceleration);
        debug("base= " + baseSpeed);
        debug("mom= " + momentum);
        debug("deltaxz= " + data.getPositionProcessor().getDeltaXZ());
        debug("ticks= " + data.getPositionProcessor().getGroundTicks());**/
        if(data.getPositionProcessor().getGroundTicks() > 20 && data.getPositionProcessor().isOnGround()) {

            if (accuracy > 0.001 && data.getPositionProcessor().getDeltaXZ() > 0.1) {
                fail("accuracy: " + accuracy, false);
            }
        }
    }

    private float getBaseSpeed(Player player, boolean sprinting) {
        float walkSpeed = (player.getWalkSpeed() / 2f);
        return sprinting ? walkSpeed + walkSpeed * 0.3f : walkSpeed;
    }

    private float getFrictionBlock(Location loc) {
        Material material = data.getPlayer().getWorld().getBlockAt(loc).getType();
        final String name = material.name();
        final boolean isIce = name.contains("ICE");
        if (name.contains("BLUE") && isIce) {
            return 0.989f;
        } else if (isIce) {
            return 0.98f;
        } else if (name.contains("SLIME")) {
            return 0.8f;
        } else if (name.contains("AIR")) {
            return 1f;
        } else {
            return 0.6f;
        }
    }

    private float getEffectMultipliers(Player player) {
        float speed = 0;
        float slowness = 0;
        for (PotionEffect pe : player.getActivePotionEffects()) {
            String name = pe.getType().getName();
            if (name.equalsIgnoreCase("SLOW")) {
                slowness = pe.getAmplifier() + 1;
            }
            if (name.equalsIgnoreCase("SPEED")) {
                speed = pe.getAmplifier() + 1;
            }
        }
        return (1 + 0.2f * speed) * (1 - 0.15f * slowness);
    }
}