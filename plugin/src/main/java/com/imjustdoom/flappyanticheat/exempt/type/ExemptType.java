package com.imjustdoom.flappyanticheat.exempt.type;

import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import com.imjustdoom.flappyanticheat.config.Config;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.util.PlayerUtil;
import io.github.retrooper.packetevents.PacketEvents;
import lombok.Getter;
import org.bukkit.GameMode;

import java.util.function.Function;

@Getter
public enum ExemptType {

    GAMEMODE(data -> data.getPlayer().getGameMode() == GameMode.SPECTATOR || data.getPlayer().getGameMode() == GameMode.CREATIVE),

    JOINED(data -> System.currentTimeMillis() - data.getJoinTime() < 5000L),

    TPS(data -> PacketEvents.get().getServerUtils().getTPS() < 17.0),

    LIQUID(data -> data.getPositionProcessor().isInLiquid()),

    SHULKER(data -> data.getPositionProcessor().isNearShulker()),

    PISTON(data -> data.getPositionProcessor().isNearPiston()),

    VELOCITY(data -> data.getVelocityProcessor().isTakingVelocity()),

    VEHICLE(data -> data.getPositionProcessor().isNearVehicle()),

    INSIDE_VEHICLE(data -> data.getPositionProcessor().getSinceVehicleTicks() < 10),

    CLIMBABLE(data -> PlayerUtil.isOnClimbable(data.getPlayer())),

    FLYING(data -> data.getPlayer().isFlying() || data.getPlayer().isGliding()),

    STEPPED(data -> data.getPositionProcessor().isOnGround() && data.getPositionProcessor().getDeltaY() > 0),

    VOID(data -> data.getPositionProcessor().getY() < 0),

    PLACING(data -> FlappyAnticheat.INSTANCE.getTickManager().getTicks() - data.getActionProcessor().getLastPlaceTick() < 10),

    ENTITIES(data -> data.getPositionProcessor().getNearbyEntities().size() > 1),

    SLIME(data -> data.getPositionProcessor().getSinceSlimeTicks() < 20),

    ON_FIRE(data -> data.getPlayer().getFireTicks() > 0);

    private final Function<FlappyPlayer, Boolean> exception;

    ExemptType(final Function<FlappyPlayer, Boolean> exception) {
        this.exception = exception;
    }
}