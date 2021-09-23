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
import com.imjustdoom.flappyanticheat.util.PlayerUtil;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.network.packet.server.play.PlayerInfoPacket;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public abstract class Check implements FlappyCheck {

    private String check, checkType, description;
    private boolean experimental, punishable, lagbackable, enabled;
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
        if(!data.getPlayer().isOnline()) return;

        // Fire FlappyFlagEvent and check if it was cancelled
        FlappyFlagEvent event = new FlappyFlagEvent(data.getPlayer(), this);
        EventDispatcher.call(event);
        boolean cancelled = event.isCancelled();
        if(cancelled) return;

        vl++;

        FlappyAnticheat.INSTANCE.getAlertExecutor().execute(() -> {

            // Create alert message
            final Component component = Component.text(MessageUtil.translate(Config.PREFIX
                            + Config.Alerts.FAILED_CHECK
                            .replaceAll("%player%", data.getPlayer().getName().toString())
                            .replaceAll("%check%", getCheck())
                            .replaceAll("%checktype%", getCheckType()))
                    .replaceAll("%vl%", String.valueOf(getVl()))
                    .replaceAll("%maxvl%", String.valueOf(getMaxVl())));

            // Create alert hover message
            component.hoverEvent(HoverEvent.showText(Component.text(MessageUtil.translate(
                    Config.Alerts.HOVER
                            .replaceAll("%description%", getDescription())
                            .replaceAll("%debug%", debug)
                            //.replaceAll("%tps%", String.valueOf(PacketEvents.get().getServerUtils().getTPS()))
                            .replaceAll("%ping%", String.valueOf(PlayerUtil.getPing(data.getPlayer())))
                    ))));

            component.clickEvent(ClickEvent.runCommand("/flappyachoverclick " + data.getPlayer().getName()));

            // Loop through players with alerts enabled
            //TODO: Improve alert toggle
            for (final UUID uuid : FlappyAnticheat.INSTANCE.getAlertManager().getToggledAlerts()) {
                Player player = MinecraftServer.getConnectionManager().findPlayer(String.valueOf(uuid));
                if (!player.hasPermission("flappyac.alerts")) continue;
                player.sendMessage(component);
            }

            // Send discord message
            /**FlappyAnticheat.INSTANCE.getApi().getTextChannelById(Config.DISCORD_BOT.CHANNEL_ID).sendMessage(
                    new EmbedBuilder()
                            .setTitle("FlappyAC Alert")
                            .addField(new MessageEmbed.Field(data.getPlayer().getName() + " flagged",
                                    getCheck() + " (" + getCheckType() + ")"
                                            + "\nViolations: " + getVl() + "/" + getMaxVl()
                                            + "\nClient Brand: " + data.getClientBrand()
                                            + "\nClient Version: " + MessageUtil.translateVersion(data.getClientVersion().name()),
                                    false))
                            .build()).queue();**/

            // Send console message
            MessageUtil.toConsole(component.toString());

            // Add violation to violation file if enabled
            if(Config.Logs.ViolationLog.ENABLED)
                FileUtil.addToFile("violations.txt",
                        Config.Logs.ViolationLog.MESSAGE
                                .replaceAll("%check%", getCheck())
                                .replaceAll("%checktype%", getCheckType())
                                .replaceAll("%player%", data.getPlayer().getName().toString())
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
            if (getVl() >= getMaxVl() && punishable) {

                // Fire FlappyPunishPlayerEvent and check if it was cancelled
                FlappyPunishPlayerEvent punishPlayerEvent = new FlappyPunishPlayerEvent(data.getPlayer(), this);
                EventDispatcher.call(punishPlayerEvent);
                boolean punishPlayerEventCancelled = punishPlayerEvent.isCancelled();
                if(punishPlayerEventCancelled) return;

                setVl(0);

                // Run punishment keys
                for (String command : getCommands()) {
                    command = command.replaceAll("%player%", data.getPlayer().getName().toString());
                    MinecraftServer.getCommandManager().getDispatcher().execute(MinecraftServer.getCommandManager().getConsoleSender(), command);
                }

                // Add punishment to punishment file if enabled
                if(Config.Logs.PunishmentLog.ENABLED)
                    FileUtil.addToFile("punishments.txt",
                            Config.Logs.PunishmentLog.MESSAGE
                                    .replaceAll("%check%", getCheck())
                                    .replaceAll("%checktype%", getCheckType())
                                    .replaceAll("%player%", data.getPlayer().getName().toString()));
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
        try {
            this.commands = config.node("punish-commands").getList(String.class);
        } catch (SerializationException e) {
            e.printStackTrace();
        }
    }
}