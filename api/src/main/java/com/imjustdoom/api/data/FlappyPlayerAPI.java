package com.imjustdoom.api.data;

import com.imjustdoom.api.check.FlappyCheck;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import org.bukkit.entity.Player;

import java.util.List;

public interface FlappyPlayerAPI {

    ClientVersion getClientVersion();

    Player getPlayer();

    long getJoinTime();

    List<FlappyCheck> getChecks();

    void addCheck(FlappyCheck check);

    void removeCheck(FlappyCheck check);
}
