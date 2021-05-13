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
                sender.sendMessage(Color.translate(FlappyAnticheat.getInstance().getConfig().getString("prefix") + FlappyAnticheat.getInstance().getConfig().getString("messages.reload")));
            } else if (args[0].equalsIgnoreCase("resetviolations")) {
                if(args.length >= 1){
                    if(Bukkit.getPlayer(args[1]) == Bukkit.getPlayerExact(args[1])) {
                        FlappyAnticheat.getInstance().violationHandler.clearViolations(Bukkit.getPlayerExact(args[1]));
                    } else {
                        sender.sendMessage(Color.translate(FlappyAnticheat.getInstance().getConfig().getString("prefix") + FlappyAnticheat.getInstance().getConfig().getString("messages.violation-remove-invalid-player")));
                    }
                } else {
                    FlappyAnticheat.getInstance().violationHandler.clearAllViolations();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.hasPermission("flappyanticheat.alerts")) {
                            p.sendMessage(Color.translate(FlappyAnticheat.getInstance().getConfig().getString("prefix") + FlappyAnticheat.getInstance().getConfig().getString("messages.violation-reset")));
                        }
                    }
                }
            } else if (args[0].equalsIgnoreCase("alerts")) {
                if(!FlappyAnticheat.getInstance().dataManager.alertsDisabled.contains(((Player) sender).getPlayer())){
                    FlappyAnticheat.getInstance().dataManager.disabledAlertsAdd(((Player) sender).getPlayer());;
                    sender.sendMessage(Color.translate(FlappyAnticheat.getInstance().getConfig().getString("prefix") + FlappyAnticheat.getInstance().getConfig().getString("messages.alert-toggle.disable")));
                } else {
                    FlappyAnticheat.getInstance().dataManager.disabledAlertsRemove(((Player) sender).getPlayer());
                    sender.sendMessage(Color.translate(FlappyAnticheat.getInstance().getConfig().getString("prefix") + FlappyAnticheat.getInstance().getConfig().getString("messages.alert-toggle.enable")));
                }
            }
        }
        return false;
    }
}
