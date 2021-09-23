package com.imjustdoom.api.data;

import com.imjustdoom.api.check.FlappyCheck;
import net.minestom.server.entity.Player;

import java.util.List;

public interface FlappyPlayerAPI {

    String getClientVersion();

    String getClientBrand();

    Player getPlayer();

    long getJoinTime();

    List<FlappyCheck> getChecks();

    void addCheck(FlappyCheck check);

    void removeCheck(FlappyCheck check);
}
