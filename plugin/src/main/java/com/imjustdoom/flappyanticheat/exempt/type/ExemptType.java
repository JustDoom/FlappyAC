package com.imjustdoom.flappyanticheat.exempt.type;

import com.imjustdoom.flappyanticheat.config.Config;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.util.PlayerUtil;
import lombok.Getter;
import net.minestom.server.entity.GameMode;

import java.util.function.Function;

@Getter
public enum ExemptType {

    GAMEMODE(player -> player.getPlayer().getGameMode() == GameMode.SPECTATOR || player.getPlayer().getGameMode() == GameMode.CREATIVE),

    JOINED(player -> System.currentTimeMillis() - player.getJoinTime() < 5000L),

    // TODO: tps exempt
    TPS(player -> 20.0 < Config.Settings.JOIN_EXEMPTION),

    LIQUID(data -> data.getPositionProcessor().isInLiquid()),

    SHULKER(data -> data.getPositionProcessor().isNearShulker()),

    PISTON(data -> data.getPositionProcessor().isNearPiston()),

    VELOCITY(data -> data.getVelocityProcessor().isTakingVelocity()),

    VEHICLE(data -> data.getPositionProcessor().isNearVehicle()),

    INSIDE_VEHICLE(data -> data.getPositionProcessor().getSinceVehicleTicks() < 10),

    CLIMBABLE(data -> data.getPositionProcessor().isOnLadder()),

    FLYING(data -> data.getPlayer().isFlying() || data.getPlayer().isFlyingWithElytra()),

    STEPPED(data -> data.getPositionProcessor().isOnGround() && data.getPositionProcessor().getDeltaY() > 0),

    VOID(data -> data.getPositionProcessor().getY() < 0);

    private final Function<FlappyPlayer, Boolean> exception;

    ExemptType(final Function<FlappyPlayer, Boolean> exception) {
        this.exception = exception;
    }
}