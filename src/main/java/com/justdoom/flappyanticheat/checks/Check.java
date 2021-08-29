package com.justdoom.flappyanticheat.checks;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.exempt.type.ExemptType;
import com.justdoom.flappyanticheat.packet.Packet;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketEvent;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Setter
public abstract class Check {

    public String check, checkType, description;
    public boolean experimental;
    public FlappyPlayer data;
    public CheckInfo checkData;
    private int maxVl, vl;

    public Check(final FlappyPlayer data) {
        this.data = data;

        this.checkData = getClass().getAnnotation(CheckInfo.class);

        this.check = checkData.check();
        this.checkType = checkData.checkType();
        this.experimental = checkData.experimental();
        this.description = checkData.description();

        loadConfigOptions();
    }

    public abstract void handle(final Packet packet);

    protected boolean isExempt(final ExemptType exemptType) {
        return data.getExemptManager().isExempt(exemptType);
    }

    protected boolean isExempt(final ExemptType... exemptTypes) {
        return data.getExemptManager().isExempt(exemptTypes);
    }

    public void fail(final String debug, final boolean lagBack) {
        vl++;

        FlappyAnticheat.INSTANCE.getAlertExecutor().execute(() -> {
            final TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&',
                    FlappyAnticheat.INSTANCE.getConfigFile().node("messages", "prefix").getString()
                            + FlappyAnticheat.INSTANCE.getConfigFile().node("messages", "failed-check").getString()
                            .replace("{player}", data.getPlayer().getName())
                            .replace("{check}", check)
                            .replace("{checkType}", checkType))
                    .replace("{vl}", String.valueOf(vl))
                    .replace("{maxvl}", String.valueOf(maxVl)));

            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',
                    FlappyAnticheat.INSTANCE.getConfigFile().node("messages", "hover").getString()
                            .replace("{description}", description)
                            .replace("{debug}", debug)
                            .replace("{tps}", String.valueOf(PacketEvents.get().getServerUtils().getTPS()))
                            .replace("{ping}", String.valueOf(PacketEvents.get().getPlayerUtils().getPing(data.getPlayer())))
                    )).create()));

            //TODO: make click command more secure
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/say clicked hehehehheheheheheheh"));


            for (final Player player : Bukkit.getOnlinePlayers()) {
                if (!player.hasPermission("flappyac.alerts") || !FlappyAnticheat.INSTANCE.getAlertManager().hasAlerts(player)) continue;
                player.spigot().sendMessage(component);
            }
            /**
             * Disabled lagBacks for now
             */
            /**if (lagBack){
                Bukkit.getScheduler().runTask(FlappyAnticheat.INSTANCE.getPlugin(), () ->
                        this.data.getPlayer().teleport(new Location(this.data.getPlayer().getWorld(),
                                this.data.getPositionProcessor().getLastX(),
                                this.data.getPositionProcessor().getLastY() - 0.05,
                                this.data.getPositionProcessor().getLastZ())));
            }**/
        });

        if (vl >= maxVl) {
            //TODO: punish
        }
    }

    public void loadConfigOptions() {
        final ConfigurationNode config = FlappyAnticheat.INSTANCE.getConfigFile();
        maxVl = config.node("checks", check.toLowerCase(), checkType.toLowerCase(), "punish-vl").getInt();
    }
}