package com.justdoom.flappyanticheat.checks;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.data.PlayerData;
import com.justdoom.flappyanticheat.utils.Color;
import com.justdoom.flappyanticheat.utils.Ping;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.utils.server.ServerUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Check extends PacketListenerAbstract {

    public String check, checkType;
    public boolean experimental;

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

    /**public Check(String check, String checkType, boolean experimental){

        this.check = check;
        this.checkType = checkType;
        this.experimental = experimental;
    }**/

    public void fail(String debug, Player player){
        if(player.hasPermission("flappyanticheat.bypass") || player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE)
            return;

        FlappyAnticheat.getInstance().violationHandler.addViolation(this, player);

        String flagmsg = FlappyAnticheat.getInstance().getConfig().getString("prefix") + FlappyAnticheat.getInstance().getConfig().getString("messages.failed-check");
        flagmsg = flagmsg.replace("{player}", player.getName()).replace("{check}", this.check + " " + checkType).replace("{vl}", String.valueOf(FlappyAnticheat.getInstance().violationHandler.getViolations(this, player)));
        String hover = FlappyAnticheat.getInstance().getConfig().getString("messages.hover").replace("{ping}", String.valueOf(Ping.getPing(player))).replace("{debug}", debug).replace("{tps}", String.valueOf(PacketEvents.get().getServerUtils().getTPS()));

        TextComponent component = new TextComponent(Color.translate(flagmsg));
        component.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Color.translate(hover)).create()));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/flappyacpunish " + player.getName()));

        for(Player p: Bukkit.getOnlinePlayers()){
            if(p.hasPermission("flappyanticheat.alerts")){
                if(!FlappyAnticheat.getInstance().dataManager.alertsDisabled.contains(p))
                    p.spigot().sendMessage(component);
            }
        }

        if(FlappyAnticheat.getInstance().getConfig().getBoolean("messages.flag-to-console")) {
            Bukkit.getConsoleSender().sendMessage(Color.translate(flagmsg));
        }

        FlappyAnticheat.getInstance().fileData.addToFile("violations.txt", "\n" + Color.translate(flagmsg + " " + debug));

        /**FlappyAnticheat.getInstance().dataManager.dataMap.values()
                .stream().filter(playerData -> data.player.hasPermission("flappyanticheat.alerts"))
                .forEach(playerData -> playerData.player.spigot().sendMessage(component));**/
    }

    public void sync(Runnable runnable) {
        AtomicBoolean waiting = new AtomicBoolean(true);
        if (FlappyAnticheat.getInstance().isEnabled()) {
            Bukkit.getScheduler().runTask(FlappyAnticheat.getInstance(), () -> {
                runnable.run();
                waiting.set(false);
            });
        }
        while (waiting.get()) {}
    }
}