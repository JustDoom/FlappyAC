package com.justdoom.flappyanticheat.checks;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.data.PlayerData;
import com.justdoom.flappyanticheat.utils.Color;
import com.justdoom.flappyanticheat.utils.Ping;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Check {

    public String check, checkType;
    public boolean experimental;

    public CheckData checkData;

    //public final PlayerData data;

    /**public Check(PlayerData data) {
        this.data = data;

        this.checkData = getClass().getAnnotation(CheckData.class);

        if (!getClass().isAnnotationPresent(CheckData.class)) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "CheckData missing in " + getClass().getSimpleName());
        }

        this.check = checkData.name();
        this.checkType = checkData.type();
        this.experimental = checkData.experimental();

    }**/

    public Check(String check, String checkType, boolean experimental){

        this.check = check;
        this.checkType = checkType;
        this.experimental = experimental;
    }

    public void fail(String debug, Player player){
        FlappyAnticheat.getInstance().violationHandler.addViolation(this, player);

        String flagmsg = FlappyAnticheat.getInstance().getConfig().getString("prefix") + FlappyAnticheat.getInstance().getConfig().getString("messages.failed-check");
        flagmsg = flagmsg.replace("{player}", player.getName()).replace("{check}", this.check + " " + checkType).replace("{vl}", String.valueOf(FlappyAnticheat.getInstance().violationHandler.getViolations(this, player)));
        String hover = FlappyAnticheat.getInstance().getConfig().getString("messages.hover").replace("{ping}", String.valueOf(Ping.getPing(player))).replace("{debug}", debug);

        TextComponent component = new TextComponent(Color.translate(flagmsg));
        component.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Color.translate(hover)).create()));

        for(Player p: Bukkit.getOnlinePlayers()){
            if(!p.hasPermission("flappyanticheat.alerts")){
                return;
            }
            p.spigot().sendMessage(component);
        }

        /**FlappyAnticheat.getInstance().dataManager.dataMap.values()
                .stream().filter(playerData -> data.player.hasPermission("flappyanticheat.alerts"))
                .forEach(playerData -> playerData.player.spigot().sendMessage(component));**/
    }
}