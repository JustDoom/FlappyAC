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
        clientVersion = PacketEvents.get().getPlayerUtils().getClientVersion(player);

        this.positionProcessor = new PositionProcessor(this);
        this.rotationProcessor = new RotationProcessor(this);
        this.velocityProcessor = new VelocityProcessor(this);
        this.settingProcessor = new SettingProcessor(this);
        this.actionProcessor = new ActionProcessor(this);
        this.exemptManager = new ExemptManager(this);

        //TODO: Improve alert toggle and join message
        for (final UUID uuid : FlappyAnticheat.INSTANCE.getAlertManager().getToggledAlerts()) {
            Player player1 = Bukkit.getPlayer(uuid);
            if (!player1.hasPermission("flappyac.alerts")) continue;
            player1.sendMessage(player.getDisplayName() + " has joined on version " + clientVersion.name());
        }

        // Fire FlappyPunishPlayerEvent and check if it was cancelled
        FlappyLoadPlayerEvent loadPlayerEvent = new FlappyLoadPlayerEvent(this);
        Bukkit.getPluginManager().callEvent(loadPlayerEvent);
    }

    @Override
    public void addCheck(FlappyCheck check) {
        for (Constructor<?> constructor : CheckManager.CONSTRUCTORS) {
            try {
                //System.out.println("check loaded");
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
                //System.out.println("check loaded");
                checks.remove((Check) constructor.newInstance(player));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public List<FlappyCheck> getChecks() {
        List<FlappyCheck> checks = new ArrayList<>();
        for(FlappyCheck check:this.checks){
            checks.add(check);
        }
        return checks;
    }
}