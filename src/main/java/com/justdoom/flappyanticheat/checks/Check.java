package com.justdoom.flappyanticheat.checks;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.concurrent.atomic.AtomicBoolean;

public class Check <E extends Event>{

    private String check, checkType;
    private boolean experimental;

    public Check(String check, String checkType, boolean experimental){
        this.check = check;
        this.checkType = checkType;
        this.experimental = experimental;
    }

    public void fail(String debug, Player player){
        String flagmsg = FlappyAnticheat.getInstance().getConfig().getString("prefix") + " " + FlappyAnticheat.getInstance().getConfig().getString("alerts.failed-check");
        flagmsg = flagmsg.replace("%player%", player.getName()).replace("%check%", this.check + " " + checkType);

        TextComponent component = new TextComponent(flagmsg);
        component.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(debug).create()));

        for(Player p: Bukkit.getOnlinePlayers()){
            if(!p.hasPermission("flappyanticheat.alerts")){
                return;
            }
            p.spigot().sendMessage(component);
        }
    }

    public void sync(Runnable runnable) {
        AtomicBoolean waiting = new AtomicBoolean(true);
            Bukkit.getScheduler().runTask(FlappyAnticheat.getInstance(), () -> {
                waiting.set(false);
            });
    }
}