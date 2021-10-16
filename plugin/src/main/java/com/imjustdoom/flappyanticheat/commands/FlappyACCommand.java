package com.imjustdoom.flappyanticheat.commands;

import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.api.check.FlappyCheck;
import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.config.Config;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.manager.AlertManager;
import com.imjustdoom.flappyanticheat.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FlappyACCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(command.getName().equalsIgnoreCase("flappyanticheat") || command.getName().equalsIgnoreCase("flappyac")){

            // Check if the command has any arguments
            if(args.length == 0) {
                sender.sendMessage("FlappyAC %version%");
                FlappyAnticheat.INSTANCE.getMenuGUI().load((Player) sender);
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "reload":
                    Config.load();

                    List<FlappyPlayer> players = new ArrayList<FlappyPlayer>(FlappyAnticheat.INSTANCE.getDataManager().getPlayers().values());
                    for(FlappyPlayer data : players){
                        for(FlappyCheck check : data.getChecks()){
                            ((Check) check).loadConfigSection();
                        }
                    }

                    sender.sendMessage(MessageUtil.translate(Config.Messages.RELOAD));
                    break;

                case "violations":
                    // Check if the right argument was used if it exists
                    if(args.length <= 1 || !args[1].equalsIgnoreCase("reset")) sender.sendMessage("Possible options are 'reset'");

                    // Reset violations for every player
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        FlappyPlayer data = FlappyAnticheat.INSTANCE.getDataManager().getData(player);
                        if(data == null) continue;

                        for(FlappyCheck check : data.getChecks()) {
                            check.setVl(0);
                        }
                    }

                    // Send the reset all violations message
                    for(UUID uuid : FlappyAnticheat.INSTANCE.getAlertManager().getToggledAlerts()) {
                        Bukkit.getPlayer(uuid).sendMessage(MessageUtil.translate(Config.Messages.RESET_ALL_VIOLATIONS));
                    }
                    break;

                case "alerts":

                    // Toggle allerts
                    FlappyAnticheat.INSTANCE.getAlertManager().toggleAlerts((Player) sender);
                    if(FlappyAnticheat.INSTANCE.getAlertManager().getToggledAlerts().contains((Player) sender))
                        sender.sendMessage(MessageUtil.translate(Config.Alerts.TOGGLE_ALERTS_OFF));
                    else
                        sender.sendMessage(MessageUtil.translate(Config.Alerts.TOGGLE_ALERTS_ON));

                    break;

                case "profile":

                    // Check if command includes a player
                    if(args.length <= 1) {
                        sender.sendMessage("You must include a player");
                        return true;
                    }

                    // Check if the player is valid
                    final Player p = Bukkit.getPlayer(args[1]);
                    if(p == null) {
                        sender.sendMessage("Invalid player");
                        return true;
                    }

                    int movement = 0, combat = 0, player = 0;
                    FlappyPlayer data = FlappyAnticheat.INSTANCE.getDataManager().getData(p);

                    // Add all the violations up
                    for(FlappyCheck check : data.getChecks()) {
                        switch (check.getCheckInfo().type()) {
                            case COMBAT:
                                combat += check.getVl();
                                break;
                            case MOVEMENT:
                                movement += check.getVl();
                                break;
                            case PLAYER:
                                player += check.getVl();
                                break;
                        }
                    }

                    // Send profile message
                    sender.sendMessage(MessageUtil.translate(Config.Messages.PROFILE)
                            .replaceAll("%version%", MessageUtil.translateVersion(data.getClientVersion().name()))
                            .replaceAll("%brand%", data.getClientBrand())
                            .replaceAll("%player%", String.valueOf(player))
                            .replaceAll("%combat%", String.valueOf(combat))
                            .replaceAll("%movement%", String.valueOf(movement))
                            .replaceAll("%total%", String.valueOf(player + movement + combat)));
                    break;
            }
        }
        return false;
    }
}