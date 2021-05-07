package com.justdoom.flappyanticheat.checks;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.data.PlayerData;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.concurrent.atomic.AtomicBoolean;

public class Check <E extends Event>{

    private String check, checkType;
    private boolean experimental;

    private CheckData checkData;

    public final PlayerData data;

    public Check(PlayerData data) {
        this.data = data;

        if (!getClass().isAnnotationPresent(CheckData.class)) {
            Bukkit.getServer().getConsoleSender().sendMessage(Color.RED + "CheckData missing in " + getClass().getSimpleName());
        }

        this.check = checkData.name();
        this.checkType = checkData.type();
        this.experimental = checkData.experimental();

    }


    public void fail(String debug, Player player){
        String flagmsg = FlappyAnticheat.getInstance().getConfig().getString("prefix") + " " + FlappyAnticheat.getInstance().getConfig().getString("alerts.failed-check");
        flagmsg = flagmsg.replace("%player%", player.getName()).replace("%check%", this.check + " " + checkType);

        TextComponent component = new TextComponent(flagmsg);
        component.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(debug).create()));

        //I did not test this so test it to find out if it works if it does not contact me
        FlappyAnticheat.getInstance().dataManager.dataMap.values()
                .stream().filter(playerData -> data.player.hasPermission("flappyanticheat.alerts"))
                .forEach(playerData -> playerData.player.spigot().sendMessage(ChatMessageType.valueOf(ChatColor.translateAlternateColorCodes('&', String.valueOf(component)))));

    }

    public void sync(Runnable runnable) {
        AtomicBoolean waiting = new AtomicBoolean(true);
            Bukkit.getScheduler().runTask(FlappyAnticheat.getInstance(), () -> {
                waiting.set(false);
            });
    }
}