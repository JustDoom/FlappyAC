package com.imjustdoom.flappyanticheat.exempt.type;

import com.imjustdoom.flappyanticheat.config.Config;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.util.PlayerUtil;
import io.github.retrooper.packetevents.PacketEvents;
import lombok.Getter;
import org.bukkit.GameMode;

import java.util.function.Function;

@Getter
public enum ExemptType {

    GAMEMODE(player -> player.getPlayer().getGameMode() == GameMode.SPECTATOR || player.getPlayer().getGameMode() == GameMode.CREATIVE),

    JOINED(player -> System.currentTimeMillis() - player.getJoinTime() < 5000L),

    TPS(player -> PacketEvents.get().getServerUtils().getTPS() < Config.Settings.JOIN_EXEMPTION),

    LIQUID(data -> data.getPositionProcessor().isInLiquid()),

    SHULKER(data -> data.getPositionProcessor().isNearShulker()),

    PISTON(data -> data.getPositionProcessor().isNearPiston()),

    VELOCITY(data -> data.getVelocityProcessor().isTakingVelocity()),

    VEHICLE(data -> data.getPositionProcessor().isNearVehicle()),

    INSIDE_VEHICLE(data -> data.getPositionProcessor().getSinceVehicleTicks() < 10),

    CLIMBABLE(data -> PlayerUtil.isOnClimbable(data.getPlayer())),

    FLYING(data -> data.getPlayer().isFlying() || data.getPlayer().isGliding());

    private final Function<FlappyPlayer, Boolean> exception;

    ExemptType(final Function<FlappyPlayer, Boolean> exception) {
        this.exception = exception;
    }
}