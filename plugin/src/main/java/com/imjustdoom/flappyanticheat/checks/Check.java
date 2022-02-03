package com.imjustdoom.flappyanticheat.checks;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.api.check.FlappyCheck;
import com.imjustdoom.api.events.FlappyFlagEvent;
import com.imjustdoom.api.events.FlappyPunishPlayerEvent;
import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import com.imjustdoom.flappyanticheat.config.Config;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;
import com.imjustdoom.flappyanticheat.util.FileUtil;
import com.imjustdoom.flappyanticheat.util.MessageUtil;
import io.github.retrooper.packetevents.utils.SpigotReflectionUtil;
import lombok.Getter;
import lombok.Setter;
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

    private String check, checkType, description;
    private boolean experimental, punishable, lagbackable, enabled, punishmentBroadcastable;
    public FlappyPlayer data;
    private CheckInfo checkData;
    private CheckType type;
    private int maxVl, vl;
    public double buffer;
    private List<String> commands;

    public Check(final FlappyPlayer data) {
        this.data = data;

        this.checkData = getClass().getAnnotation(CheckInfo.class);

        this.check = checkData.check();
        this.checkType = checkData.checkType();
        this.experimental = checkData.experimental();
        this.description = checkData.description();
        this.type = checkData.type();

        loadConfigSection();
    }

    public abstract void handle(final Packet packet);

    public void fail(final String debug, final boolean lagBack) {
        // Make sure player is online because punishing has some issues with this
        if (!data.getPlayer().isOnline()) return;

        // Fire FlappyPreFlagEvent and check if it was cancelled
        FlappyFlagEvent flagEvent = new FlappyFlagEvent(data.getPlayer(), this);
        Bukkit.getPluginManager().callEvent(flagEvent);
        if (flagEvent.isCancelled()) return;

        vl++;

        FlappyAnticheat.INSTANCE.getAlertExecutor().execute(() -> {

            // Create alert message
            final TextComponent component = new TextComponent(MessageUtil.translate(Config.PREFIX
                            + Config.Alerts.FAILED_CHECK
                            .replaceAll("%player%", data.getPlayer().getName())
                            .replaceAll("%check%", getCheck())
                            .replaceAll("%checktype%", getCheckType()))
                    .replaceAll("%vl%", String.valueOf(getVl()))
                    .replaceAll("%maxvl%", String.valueOf(getMaxVl())));

            // Create alert hover message
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(MessageUtil.translate(
                    Config.Alerts.HOVER
                            .replaceAll("%description%", getDescription())
                            .replaceAll("%debug%", debug)
                            //TODO: tps check eeeeeeeeeeeeee
                            .replaceAll("%tps%", String.valueOf(SpigotReflectionUtil.recentTPS()[0]))
                            .replaceAll("%ping%", String.valueOf(SpigotReflectionUtil.getPlayerPingLegacy(data.getPlayer())))
            )).create()));

            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/flappyachoverclick " + data.getPlayer().getName()));

            // Loop through players with alerts enabled
            //TODO: Improve alert toggle
            for (final UUID uuid : FlappyAnticheat.INSTANCE.getAlertManager().getToggledAlerts()) {
                Player player = Bukkit.getPlayer(uuid);
                if (!player.hasPermission("flappyac.alerts")) continue;
                player.spigot().sendMessage(component);
            }

            // Send console message
            MessageUtil.toConsole(component.getText());

            // Add violation to violation file if enabled
            if (Config.Logs.ViolationLog.ENABLED)
                FileUtil.addToFile("violations.txt",
                        Config.Logs.ViolationLog.MESSAGE
                                .replaceAll("%check%", getCheck())
                                .replaceAll("%checktype%", getCheckType())
                                .replaceAll("%player%", data.getPlayer().getName())
                                .replaceAll("%vl%", String.valueOf(getVl()))
                                .replaceAll("%maxvl%", String.valueOf(getMaxVl())));

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

            // TODO: punishment announcement
            if (getVl() >= getMaxVl() && punishable) {

                // Fire FlappyPunishPlayerEvent and check if it was cancelled
                FlappyPunishPlayerEvent punishEvent = new FlappyPunishPlayerEvent(data.getPlayer(), this);
                Bukkit.getPluginManager().callEvent(punishEvent);
                if (punishEvent.isCancelled()) return;

                setVl(0);

                // Run punishment keys
                for (String command : getCommands()) {
                    command = command.replaceAll("%player%", data.getPlayer().getName());
                    String finalCommand = command;
                    Bukkit.getScheduler().runTask(FlappyAnticheat.INSTANCE.getPlugin(), () ->
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand));
                }

                // Add punishment to punishment file if enabled
                if (Config.Logs.PunishmentLog.ENABLED)
                    FileUtil.addToFile("punishments.txt",
                            Config.Logs.PunishmentLog.MESSAGE
                                    .replaceAll("%check%", getCheck())
                                    .replaceAll("%checktype%", getCheckType())
                                    .replaceAll("%player%", data.getPlayer().getName()));
            }
        });
    }

    /**
     * Send debug message to player flagging check only if they have notify permission
     * helps stop random debug messages being sent to any random player
     *
     * @param debug
     */
    public void debug(String debug) {
        if (!data.getPlayer().hasPermission("flappyac.alerts")) return;
        data.getPlayer().sendMessage(debug);
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

    protected boolean isExempt(final ExemptType exemptType) {
        return data.getExemptManager().isExempt(exemptType);
    }

    protected boolean isExempt(final ExemptType... exemptTypes) {
        return data.getExemptManager().isExempt(exemptTypes);
    }

    public void loadConfigSection() {
        ConfigurationNode config = Config.getConfigurationSection(check.toLowerCase(), checkType.toLowerCase());

        this.maxVl = config.node("punish-vl").getInt();
        this.punishable = config.node("punishable").getBoolean();
        this.enabled = config.node("enabled").getBoolean();
        this.punishmentBroadcastable = config.node("broadcast-punishment").getBoolean();
        try {
            this.commands = config.node("punish-commands").getList(String.class);
        } catch (SerializationException e) {
            e.printStackTrace();
        }
    }
}