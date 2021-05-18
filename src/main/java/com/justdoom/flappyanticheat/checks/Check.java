package com.justdoom.flappyanticheat.checks;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.customevents.FlagEvent;
import com.justdoom.flappyanticheat.customevents.PunishEvent;
import com.justdoom.flappyanticheat.utils.Color;
import com.justdoom.flappyanticheat.utils.PlayerUtil;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicBoolean;

public class Check extends PacketListenerAbstract {

    public String check, checkType;
    public boolean experimental;

    public Check(String check, String checkType, boolean experimental) {

        this.check = check;
        this.checkType = checkType;
        this.experimental = experimental;
    }

    public void fail(String debug, Player player) {
        FlagEvent flagEvent = new FlagEvent(player, this);
        Bukkit.getPluginManager().callEvent(flagEvent);
        if(flagEvent.isCancelled()) {
            return;
        }

        if (player.hasPermission("flappyanticheat.bypass") || player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE)
            return;

        FlappyAnticheat.getInstance().violationHandler.addViolation(this, player);

        String flagmsg = FlappyAnticheat.getInstance().getConfig().getString("prefix") + FlappyAnticheat.getInstance().getConfig().getString("messages.failed-check");
        flagmsg = flagmsg.replace("{player}", player.getName()).replace("{check}", this.check + " " + checkType).replace("{vl}", String.valueOf(FlappyAnticheat.getInstance().violationHandler.getViolations(this, player)));
        String hover = FlappyAnticheat.getInstance().getConfig().getString("messages.hover").replace("{ping}", String.valueOf(PlayerUtil.getPing(player))).replace("{debug}", debug).replace("{tps}", String.valueOf(PacketEvents.get().getServerUtils().getTPS()));

        if(experimental){
            flagmsg = flagmsg+"&r*";
        }

        TextComponent component = new TextComponent(Color.translate(flagmsg));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Color.translate(hover)).create()));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/flappyacflagclick " + player.getName()));

        FlappyAnticheat.getInstance().dataManager.dataMap.values()
                .stream().filter(playerData -> player.hasPermission("flappyanticheat.alerts") && !FlappyAnticheat.getInstance().dataManager.alertsDisabled.contains(player))
                .forEach(playerData -> playerData.player.spigot().sendMessage(component));

        if (FlappyAnticheat.getInstance().getConfig().getBoolean("messages.flag-to-console")) {
            Bukkit.getConsoleSender().sendMessage(Color.translate(flagmsg));
        }

        FlappyAnticheat.getInstance().fileData.addToFile("violations.txt", "\n" + Color.translate(flagmsg + " " + debug));
    }

    public void punish(Player player, String path){
        PunishEvent punishEvent = new PunishEvent(player, this);
        Bukkit.getPluginManager().callEvent(punishEvent);
        if(punishEvent.isCancelled()) {
            return;
        }

        for(String command: FlappyAnticheat.getInstance().getConfig().getStringList(path + ".punish-commands")) {
            command = command.replace("{player}", player.getName());
            String finalCommand = command;
            Bukkit.getScheduler().runTask(FlappyAnticheat.getInstance(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand));
        }
        if(FlappyAnticheat.getInstance().getConfig().getBoolean(path + ".broadcast-punishment")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(Color.translate(FlappyAnticheat.getInstance().getConfig().getString("messages.punish")).replace("{player}", player.getName()));
            }
        }
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