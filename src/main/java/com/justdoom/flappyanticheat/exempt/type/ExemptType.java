package com.justdoom.flappyanticheat.exempt.type;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import io.github.retrooper.packetevents.PacketEvents;
import lombok.Getter;
import org.bukkit.GameMode;

import java.util.function.Function;

@Getter
public enum ExemptType {

    GAMEMODE(player -> player.getPlayer().getGameMode() == GameMode.SPECTATOR || player.getPlayer().getGameMode() == GameMode.CREATIVE),

    TPS(player -> PacketEvents.get().getServerUtils().getTPS() < 18.5);

    private final Function<FlappyPlayer, Boolean> exception;

    ExemptType(final Function<FlappyPlayer, Boolean> exception) {
        this.exception = exception;
    }
}