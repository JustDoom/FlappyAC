package com.justdoom.flappyanticheat.checks;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.exempt.type.ExemptType;
import com.justdoom.flappyanticheat.packet.Packet;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Color;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Setter
public abstract class Check {

    public String check, checkType;
    public boolean experimental;

    public FlappyPlayer player;

    public CheckInfo checkData;

    public Check(FlappyPlayer player) {
        this.player = player;

        this.checkData = getClass().getAnnotation(CheckInfo.class);

        this.check = checkData.check();
        this.checkType = checkData.checkType();
        this.experimental = checkData.experimental();
    }

    public abstract void handle(final Packet packet);

    protected boolean isExempt(final ExemptType exemptType) {
        return player.getExemptManager().isExempt(exemptType);
    }

    protected boolean isExempt(final ExemptType... exemptTypes) {
        return player.getExemptManager().isExempt(exemptTypes);
    }

    public void fail(String ... info){

        String msg = "You are hacking " + check + checkType;

        TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', msg));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', Arrays.toString(info))).create()));

        player.getPlayer().spigot().sendMessage(component);
    }

    public void sync(Runnable runnable) {
        AtomicBoolean waiting = new AtomicBoolean(true);
        if (FlappyAnticheat.getInstance().isEnabled()) {
            Bukkit.getScheduler().runTask(FlappyAnticheat.getInstance(), () -> {
                runnable.run();
                waiting.set(false);
            });
        }
        while (waiting.get()) {
        }
    }
}