package com.justdoom.flappyanticheat.commands;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlappyACCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(command.getName().equalsIgnoreCase("flappyanticheat")){
            if(args.length == 0){
                sender.sendMessage("Please do /flappyac help got help");
            } else if (args[0].equalsIgnoreCase("reload")) {
                FlappyAnticheat.getInstance().reloadConfig();
                sender.sendMessage("FlappyAC config reloaded");
            } else if (args[0].equalsIgnoreCase("resetviolations")) {
                FlappyAnticheat.getInstance().violationHandler.clearAllViolations();
                for(Player p: Bukkit.getOnlinePlayers()){
                    if(p.hasPermission("flappyanticheat.alerts")){
                        p.sendMessage(Color.translate(FlappyAnticheat.getInstance().getConfig().getString("prefix") + FlappyAnticheat.getInstance().getConfig().getString("messages.violation-reset")));
                    }
                }
            }
        }
        return false;
    }
}
