package com.imjustdoom.flappyanticheat.exempt.type;

import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.util.PlayerUtil;
import io.github.retrooper.packetevents.utils.SpigotReflectionUtil;
import lombok.Getter;
import org.bukkit.GameMode;

import java.util.function.Function;

@Getter
public enum ExemptType {

    GAMEMODE(data -> data.getPlayer().getGameMode() == GameMode.SPECTATOR || data.getPlayer().getGameMode() == GameMode.CREATIVE),

    JOINED(data -> System.currentTimeMillis() - data.getJoinTime() < 5000L),


    //TODO: check if tps value is correct
    TPS(data -> SpigotReflectionUtil.recentTPS()[0] < 17.0),

    LIQUID(data -> data.getFlyingProcessor().isInLiquid()),

    SHULKER(data -> data.getFlyingProcessor().isNearShulker()),

    PISTON(data -> data.getFlyingProcessor().isNearPiston()),

    VELOCITY(data -> data.getVelocityProcessor().isTakingVelocity()),

    VEHICLE(data -> data.getFlyingProcessor().isNearVehicle()),

    INSIDE_VEHICLE(data -> data.getFlyingProcessor().getSinceVehicleTicks() < 10),

    CLIMBABLE(data -> PlayerUtil.isOnClimbable(data.getPlayer())),

    FLYING(data -> data.getPlayer().isFlying() || data.getPlayer().isGliding()),

    STEPPED(data -> data.getFlyingProcessor().isOnGround() && data.getFlyingProcessor().getDeltaY() > 0),

    VOID(data -> data.getFlyingProcessor().getY() < 0),

    PLACING(data -> FlappyAnticheat.INSTANCE.getTickManager().getTicks() - data.getActionProcessor().getLastPlaceTick() < 10),

    ENTITIES(data -> data.getFlyingProcessor().getNearbyEntities().size() > 1),

    SLIME(data -> data.getFlyingProcessor().getSinceSlimeTicks() < 20),

    ON_FIRE(data -> data.getPlayer().getFireTicks() > 0);

    private final Function<FlappyPlayer, Boolean> exception;

    ExemptType(final Function<FlappyPlayer, Boolean> exception) {
        this.exception = exception;
    }
}