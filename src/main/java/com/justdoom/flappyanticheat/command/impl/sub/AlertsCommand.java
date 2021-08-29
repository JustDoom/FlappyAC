package com.justdoom.flappyanticheat.command.impl.sub;

import com.justdoom.flappyanticheat.command.framework.Command;
import com.justdoom.flappyanticheat.command.framework.CommandArgs;
import com.justdoom.flappyanticheat.command.impl.FlappyCommand;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;

public class AlertsCommand extends FlappyCommand {
    @Command(name = "alerts", permission = "flappy.commands.alerts")
    public void onCommand(CommandArgs command){
        this.plugin.getAlertManager().toggleAlerts(command.getPlayer());
        if (this.plugin.getAlertManager().hasAlerts(command.getPlayer())){
            command.getPlayer().sendMessage(ChatColor.GREEN + "Enabled FlappyAC alerts.");
        } else {
            command.getPlayer().sendMessage(ChatColor.RED + "Disabled FlappyAC alerts.");
        }
    }
}
