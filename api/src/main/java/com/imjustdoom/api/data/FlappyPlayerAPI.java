package com.imjustdoom.api.data;

import io.github.retrooper.packetevents.utils.player.ClientVersion;
import org.bukkit.entity.Player;

public interface FlappyPlayerAPI {

    ClientVersion getClientVersion();

    Player getPlayer();
}
