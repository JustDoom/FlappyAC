package com.justdoom.flappyanticheat.checks;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.exempt.type.ExemptType;
import com.justdoom.flappyanticheat.packet.Packet;
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

import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Setter
public abstract class Check {

    public String check, checkType, description;
    public boolean experimental;
    public FlappyPlayer player;
    public CheckInfo checkData;
    private int maxVl, vl;

    public Check(final FlappyPlayer player) {
        this.player = player;

        this.checkData = getClass().getAnnotation(CheckInfo.class);

        this.check = checkData.check();
        this.checkType = checkData.checkType();
        this.experimental = checkData.experimental();
        this.description = checkData.description();

        loadConfigOptions();
    }

    public abstract void handle(final Packet packet);

    protected boolean isExempt(final ExemptType exemptType) {
        return player.getExemptManager().isExempt(exemptType);
    }

    protected boolean isExempt(final ExemptType... exemptTypes) {
        return player.getExemptManager().isExempt(exemptTypes);
    }

    public void fail(final String info) {
        vl++;

        FlappyAnticheat.INSTANCE.getAlertExecutor().execute(() -> {
            final TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&',
                    FlappyAnticheat.INSTANCE.getConfigFile().node("messages", "prefix")
                            + FlappyAnticheat.INSTANCE.getConfigFile().node("messages", "failed-check").getString()
                            .replace("{player}", player.getPlayer().getName())
                            .replace("{check}", check + checkType))
                    .replace("{vl}", String.valueOf(vl))
                    .replace("{maxvl}", String.valueOf(maxVl)));

            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',
                    FlappyAnticheat.INSTANCE.getConfigFile().node("messages", "hover").getString())).create()));
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/say clicked hehehehheheheheheheh"));


            for (final Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("flappyac.alerts")) {
                    player.spigot().sendMessage(component);
                }
            }
        });

        if (vl >= maxVl) {
            //TODO: punish
        }
    }

    public void loadConfigOptions() {
        final ConfigurationNode config = FlappyAnticheat.INSTANCE.getConfigFile();
        maxVl = config.node("checks", check.toLowerCase(), checkType.toLowerCase(), "punish-vl").getInt();
    }

    public void sync(final Runnable runnable) {
        final AtomicBoolean waiting = new AtomicBoolean(true);

        if (FlappyAnticheat.INSTANCE.getPlugin().isEnabled()) {
            Bukkit.getScheduler().runTask(FlappyAnticheat.INSTANCE.getPlugin(), () -> {
                runnable.run();
                waiting.set(false);
            });
        }

        while (waiting.get()) {
        }
    }
}