package com.imjustdoom.flappyanticheat.data;

import com.imjustdoom.api.check.FlappyCheck;
import com.imjustdoom.api.data.FlappyPlayerAPI;
import com.imjustdoom.api.events.FlappyLoadPlayerEvent;
import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.processor.*;
import com.imjustdoom.flappyanticheat.manager.CheckManager;
import com.imjustdoom.flappyanticheat.exempt.ExemptManager;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class FlappyPlayer implements FlappyPlayerAPI {

    private final Player player;
    private final ClientVersion clientVersion;
    private String clientBrand;

    private final long joinTime = System.currentTimeMillis();
    private final List<FlappyCheck> checks = CheckManager.loadChecks(this);

    private final PositionProcessor positionProcessor;
    private final RotationProcessor rotationProcessor;
    private final VelocityProcessor velocityProcessor;
    private final SettingProcessor settingProcessor;
    private final ActionProcessor actionProcessor;
    private final ExemptManager exemptManager;

    public FlappyPlayer(Player player){

        this.player = player;
        this.clientVersion = PacketEvents.get().getPlayerUtils().getClientVersion(player);

        // Load the processors and exempt manager
        this.positionProcessor = new PositionProcessor(this);
        this.rotationProcessor = new RotationProcessor(this);
        this.velocityProcessor = new VelocityProcessor(this);
        this.settingProcessor = new SettingProcessor(this);
        this.actionProcessor = new ActionProcessor(this);
        this.exemptManager = new ExemptManager(this);

        //TODO: Improve alert toggle and join message

        // Fire FlappyLoadPlayerEvent
        FlappyLoadPlayerEvent loadPlayerEvent = new FlappyLoadPlayerEvent(this);
        Bukkit.getPluginManager().callEvent(loadPlayerEvent);
    }

    @Override
    public void addCheck(FlappyCheck check) {
        for (Constructor<?> constructor : CheckManager.CONSTRUCTORS) {
            try {
                checks.add((Check) constructor.newInstance(player));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void removeCheck(FlappyCheck check) {
        for (Constructor<?> constructor : CheckManager.CONSTRUCTORS) {
            try {
                checks.remove((Check) constructor.newInstance(player));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public List<FlappyCheck> getChecks() {
        return new ArrayList<>(this.checks);
    }
}