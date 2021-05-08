package com.justdoom.flappyanticheat.checks;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.data.PlayerData;
import com.justdoom.flappyanticheat.utils.Color;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.concurrent.atomic.AtomicBoolean;

public class Check {

    private String check, checkType;
    private boolean experimental;

    public CheckData checkData;

    public final PlayerData data;

    public Check(PlayerData data) {
        this.data = data;

        this.checkData = getClass().getAnnotation(CheckData.class);

        if (!getClass().isAnnotationPresent(CheckData.class)) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "CheckData missing in " + getClass().getSimpleName());
        }

        this.check = checkData.name();
        this.checkType = checkData.type();
        this.experimental = checkData.experimental();

    }

    public void fail(String debug, Player player){
        String flagmsg = FlappyAnticheat.getInstance().getConfig().getString("prefix") + " " + FlappyAnticheat.getInstance().getConfig().getString("alerts.failed-check");
        flagmsg = flagmsg.replace("{player}", player.getName()).replace("{check}", this.check + " " + checkType);
        String hover = FlappyAnticheat.getInstance().getConfig().getString("alerts.hover").replace("{ping}", "add later").replace("{debug}", debug);

        TextComponent component = new TextComponent(Color.translate(flagmsg));
        component.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));

        FlappyAnticheat.getInstance().dataManager.dataMap.values()
                .stream().filter(playerData -> data.player.hasPermission("flappyanticheat.alerts"))
                .forEach(playerData -> playerData.player.spigot().sendMessage(component));

    }
}