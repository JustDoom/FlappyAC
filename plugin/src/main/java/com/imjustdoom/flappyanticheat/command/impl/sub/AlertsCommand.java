package com.imjustdoom.flappyanticheat.command.impl.sub;

import com.imjustdoom.flappyanticheat.command.framework.Command;
import com.imjustdoom.flappyanticheat.command.framework.CommandArgs;
import com.imjustdoom.flappyanticheat.command.impl.FlappyCommand;
import org.bukkit.ChatColor;

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
