package com.imjustdoom.api.data;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.imjustdoom.api.check.FlappyCheck;
import org.bukkit.entity.Player;

import java.util.List;

public interface FlappyPlayerAPI {

    ClientVersion getClientVersion();

    String getClientBrand();

    Player getPlayer();

    long getJoinTime();

    List<FlappyCheck> getChecks();

    void addCheck(FlappyCheck check);

    void removeCheck(FlappyCheck check);
}
