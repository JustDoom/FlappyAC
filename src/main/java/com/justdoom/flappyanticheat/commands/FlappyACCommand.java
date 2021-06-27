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
                sender.sendMessage(Color.translate(FlappyAnticheat.getInstance().getConfig().getString("prefix") + "&7Please do \"/flappyac help\" for the help command"));

            //Reload
            } else if (args[0].equalsIgnoreCase("reload")) {
                FlappyAnticheat.getInstance().reloadConfig();
                sender.sendMessage(Color.translate(FlappyAnticheat.getInstance().getConfig().getString("prefix") + FlappyAnticheat.getInstance().getConfig().getString("messages.reload")));

            //Reset Violations
            } else if (args[0].equalsIgnoreCase("resetviolations")) {
                if(args.length >= 2){
                    if(Bukkit.getPlayerExact(args[1]) != null) {
                        FlappyAnticheat.getInstance().violationHandler.clearViolations(Bukkit.getPlayerExact(args[1]));
                        for(Player p: Bukkit.getOnlinePlayers()){
                            if(p.hasPermission("flappyanticheat.alerts")){
                                p.sendMessage(Color.translate(FlappyAnticheat.getInstance().getConfig().getString("prefix") + FlappyAnticheat.getInstance().getConfig().getString("messages.violation-reset.player").replace("{player}", args[1])));
                            }
                        }
                        if(FlappyAnticheat.getInstance().getConfig().getBoolean("messages.flag-to-console")) {
                            Bukkit.getConsoleSender().sendMessage(Color.translate(FlappyAnticheat.getInstance().getConfig().getString("prefix") + FlappyAnticheat.getInstance().getConfig().getString("messages.violation-reset.player").replace("{player}", args[1])));
                        }
                    } else {
                        sender.sendMessage(Color.translate(FlappyAnticheat.getInstance().getConfig().getString("prefix") + FlappyAnticheat.getInstance().getConfig().getString("messages.violation-remove-invalid-player")));
                    }
                } else {
                    FlappyAnticheat.getInstance().violationHandler.clearAllViolations();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.hasPermission("flappyanticheat.alerts")) {
                            p.sendMessage(Color.translate(FlappyAnticheat.getInstance().getConfig().getString("prefix") + FlappyAnticheat.getInstance().getConfig().getString("messages.violation-reset.all")));
                        }
                    }
                    if(FlappyAnticheat.getInstance().getConfig().getBoolean("messages.flag-to-console")) {
                        Bukkit.getConsoleSender().sendMessage(Color.translate(FlappyAnticheat.getInstance().getConfig().getString("prefix") + FlappyAnticheat.getInstance().getConfig().getString("messages.violation-reset.all")));
                    }
                }

                //Alerts toggle
            } else if (args[0].equalsIgnoreCase("alerts")) {
                if(!FlappyAnticheat.getInstance().dataManager.alertsDisabled.contains(((Player) sender).getPlayer())){
                    FlappyAnticheat.getInstance().dataManager.disabledAlertsAdd(((Player) sender).getPlayer());
                    sender.sendMessage(Color.translate(FlappyAnticheat.getInstance().getConfig().getString("prefix") + FlappyAnticheat.getInstance().getConfig().getString("messages.alert-toggle.disable")));
                } else {
                    FlappyAnticheat.getInstance().dataManager.disabledAlertsRemove(((Player) sender).getPlayer());
                    sender.sendMessage(Color.translate(FlappyAnticheat.getInstance().getConfig().getString("prefix") + FlappyAnticheat.getInstance().getConfig().getString("messages.alert-toggle.enable")));
                }

                //Player profile
            } else if (args[0].equalsIgnoreCase("profile")){
                if(args.length > 1){
                    Player targetPlayer = Bukkit.getPlayer(args[1]);
                    //String clientBrand =
                }
            }
        }
        return false;
    }
}
