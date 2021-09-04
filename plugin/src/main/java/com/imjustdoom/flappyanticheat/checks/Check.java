package com.imjustdoom.flappyanticheat.checks;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.FlappyCheck;
import com.imjustdoom.api.events.FlappyFlagEvent;
import com.imjustdoom.api.events.FlappyPunishPlayerEvent;
import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;
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
        // Make sure player is online
        if(!data.getPlayer().isOnline()) return;

        // Fire FlappyFlagEvent and check if it was cancelled
        FlappyFlagEvent flagEvent = new FlappyFlagEvent(data.getPlayer(), this);
        Bukkit.getPluginManager().callEvent(flagEvent);
        if(flagEvent.isCancelled()) return;

        vl++;

        FlappyAnticheat.INSTANCE.getAlertExecutor().execute(() -> {

            // Alert message
            final TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&',
                    FlappyAnticheat.INSTANCE.getConfigFile().node("messages", "prefix").getString()
                            + FlappyAnticheat.INSTANCE.getConfigFile().node("messages", "failed-check").getString()
                            .replace("{player}", data.getPlayer().getName())
                            .replace("{check}", check)
                            .replace("{checktype}", checkType))
                    .replace("{vl}", String.valueOf(vl))
                    .replace("{maxvl}", String.valueOf(maxVl)));

            // Alert hover message
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',
                    FlappyAnticheat.INSTANCE.getConfigFile().node("messages", "hover").getString()
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

            if(FlappyAnticheat.INSTANCE.getConfigFile().node("logs", "violation-log", "enabled").getBoolean())
                FileUtil.addToFile("violations.txt",
                        FlappyAnticheat.INSTANCE.getConfigFile().node("logs", "violation-log", "message").getString()
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
            if (vl >= maxVl && FlappyAnticheat.INSTANCE.getConfigFile().node("checks", check.toLowerCase(),
                    checkType.toLowerCase(), "punishable").getBoolean()) {

                // Fire FlappyPunishPlayerEvent and check if it was cancelled
                FlappyPunishPlayerEvent punishEvent = new FlappyPunishPlayerEvent(data.getPlayer(), this);
                Bukkit.getPluginManager().callEvent(punishEvent);
                if(punishEvent.isCancelled()) return;

                vl = 0;

                // Run punishment keys
                try {
                    for (String command : FlappyAnticheat.INSTANCE.getConfigFile().node("checks", check.toLowerCase(),
                            checkType.toLowerCase(), "punish-commands").getList(String.class)) {
                        command = command.replace("{player}", data.getPlayer().getName());
                        String finalCommand = command;
                        Bukkit.getScheduler().runTask(FlappyAnticheat.INSTANCE.getPlugin(), () ->
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand));
                    }
                } catch (SerializationException e){
                    e.printStackTrace();
                }

                if(FlappyAnticheat.INSTANCE.getConfigFile().node("logs", "punishment-log", "enabled").getBoolean())
                    FileUtil.addToFile("punishments.txt",
                            FlappyAnticheat.INSTANCE.getConfigFile().node("logs", "punishment-log", "message").getString()
                                    .replace("{check}", check)
                                    .replace("{checktype}", checkType)
                                    .replace("{player}", data.getPlayer().getName()));
            }
        });
    }

    private void loadConfigOptions() {
        final ConfigurationNode config = FlappyAnticheat.INSTANCE.getConfigFile();
        maxVl = config.node("checks", check.toLowerCase(), checkType.toLowerCase(), "punish-vl").getInt();
        punishable = config.node("checks", check.toLowerCase(), checkType.toLowerCase(), "punishable").getBoolean();
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