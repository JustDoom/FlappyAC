package com.imjustdoom.flappyanticheat.checks;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.FlappyCheck;
import com.imjustdoom.api.events.FlappyFlagEvent;
import com.imjustdoom.api.events.FlappyPunishPlayerEvent;
import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import com.imjustdoom.flappyanticheat.config.Config;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;
import com.imjustdoom.flappyanticheat.util.Color;
import com.imjustdoom.flappyanticheat.util.FileUtil;
import io.github.retrooper.packetevents.PacketEvents;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public abstract class Check implements FlappyCheck {

    public String check, checkType, description;
    public boolean experimental, punishable, lagbackable;
    public FlappyPlayer data;
    public CheckInfo checkData;
    private int maxVl, vl;
    public double buffer;
    public List<String> commands;

    public Check(final FlappyPlayer data) {
        this.data = data;

        this.checkData = getClass().getAnnotation(CheckInfo.class);

        this.check = checkData.check();
        this.checkType = checkData.checkType();
        this.experimental = checkData.experimental();
        this.description = checkData.description();

        ConfigurationNode config = Config.getConfigurationSection(check.toLowerCase(), checkType.toLowerCase());

        this.maxVl = config.node("punish-vl").getInt();
        this.punishable = config.node("punishable").getBoolean();
        try {
            this.commands = config.node("punish-commands").getList(String.class);
        } catch (SerializationException e) {
            e.printStackTrace();
        }
    }

    public abstract void handle(final Packet packet);

    protected boolean isExempt(final ExemptType exemptType) {
        return data.getExemptManager().isExempt(exemptType);
    }

    protected boolean isExempt(final ExemptType... exemptTypes) {
        return data.getExemptManager().isExempt(exemptTypes);
    }

    public void fail(final String debug, final boolean lagBack) {
        // Make sure player is online
        if(!data.getPlayer().isOnline()) return;

        // Fire FlappyFlagEvent and check if it was cancelled
        FlappyFlagEvent flagEvent = new FlappyFlagEvent(data.getPlayer(), this);
        Bukkit.getPluginManager().callEvent(flagEvent);
        if(flagEvent.isCancelled()) return;

        vl++;

        FlappyAnticheat.INSTANCE.getAlertExecutor().execute(() -> {

            // Alert message
            final TextComponent component = new TextComponent(Color.translate(Config.PREFIX
                            + Config.Alerts.FAILED_CHECK
                            .replace("{player}", data.getPlayer().getName())
                            .replace("{check}", check)
                            .replace("{checktype}", checkType))
                    .replace("{vl}", String.valueOf(vl))
                    .replace("{maxvl}", String.valueOf(maxVl)));

            // Alert hover message
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Color.translate(
                    Config.Alerts.HOVER
                            .replace("{description}", description)
                            .replace("{debug}", debug)
                            .replace("{tps}", String.valueOf(PacketEvents.get().getServerUtils().getTPS()))
                            .replace("{ping}", String.valueOf(PacketEvents.get().getPlayerUtils().getPing(data.getPlayer())))
                    )).create()));

            //TODO: make click command more secure
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/say clicked hehehehheheheheheheh"));

            // Loop through players with alerts enabled
            //TODO: Improve alert toggle
            for (final UUID uuid : FlappyAnticheat.INSTANCE.getAlertManager().getToggledAlerts()) {
                Player player = Bukkit.getPlayer(uuid);
                if (!player.hasPermission("flappyac.alerts")) continue;
                player.spigot().sendMessage(component);
            }

            if(Config.Logs.ViolationLog.ENABLED)
                FileUtil.addToFile("violations.txt",
                        Config.Logs.ViolationLog.MESSAGE
                                .replace("{check}", check)
                                .replace("{checktype}", checkType)
                                .replace("{player}", data.getPlayer().getName())
                                .replace("{vl}", String.valueOf(vl))
                                .replace("{maxvl}", String.valueOf(maxVl)));

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

            // Check if violations are equal to or bigger than the max violations
            if (vl >= maxVl && punishable) {

                // Fire FlappyPunishPlayerEvent and check if it was cancelled
                FlappyPunishPlayerEvent punishEvent = new FlappyPunishPlayerEvent(data.getPlayer(), this);
                Bukkit.getPluginManager().callEvent(punishEvent);
                if(punishEvent.isCancelled()) return;

                vl = 0;

                // Run punishment keys
                for (String command : commands) {
                    command = command.replace("{player}", data.getPlayer().getName());
                    String finalCommand = command;
                    Bukkit.getScheduler().runTask(FlappyAnticheat.INSTANCE.getPlugin(), () ->
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand));
                }

                if(Config.Logs.PunishmentLog.ENABLED)
                    FileUtil.addToFile("punishments.txt",
                            Config.Logs.PunishmentLog.MESSAGE
                                    .replace("{check}", check)
                                    .replace("{checktype}", checkType)
                                    .replace("{player}", data.getPlayer().getName()));
            }
        });
    }

    // Credit to medusa anticheat
    public CheckInfo getCheckInfo() {
        if (this.getClass().isAnnotationPresent(CheckInfo.class)) {
            return this.getClass().getAnnotation(CheckInfo.class);
        } else {
            System.err.println("CheckInfo annotation hasn't been added to the class " + this.getClass().getSimpleName() + ".");
        }
        return null;
    }
}