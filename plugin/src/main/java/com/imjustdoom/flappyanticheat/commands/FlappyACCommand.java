package com.imjustdoom.flappyanticheat.commands;

import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.api.check.FlappyCheck;
import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.config.Config;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FlappyACCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(command.getName().equalsIgnoreCase("flappyanticheat") || command.getName().equalsIgnoreCase("flappyac")){

            if(args.length == 0) {
                sender.sendMessage("FlappyAC {version}");
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "reload":
                    Config.load();
                    sender.sendMessage(MessageUtil.translate(Config.Messages.RELOAD));
                    break;

                case "violations":
                    if(args.length <= 1 || !args[1].equalsIgnoreCase("reset")) sender.sendMessage("Possible options are 'reset'");

                    for(Player player : Bukkit.getOnlinePlayers()) {
                        FlappyPlayer data = FlappyAnticheat.INSTANCE.getDataManager().getData(player);
                        if(data == null) continue;

                        for(FlappyCheck check : data.getChecks()) {
                            check.setVl(0);
                        }
                    }

                    for(UUID uuid : FlappyAnticheat.INSTANCE.getAlertManager().getToggledAlerts()) {
                        Bukkit.getPlayer(uuid).sendMessage(MessageUtil.translate(Config.Messages.RESET_ALL_VIOLATIONS));
                    }
                    break;

                case "alerts":
                    FlappyAnticheat.INSTANCE.getAlertManager().toggleAlerts((Player) sender);
                    if(FlappyAnticheat.INSTANCE.getAlertManager().getToggledAlerts().contains((Player) sender))
                        sender.sendMessage(MessageUtil.translate(Config.Alerts.TOGGLE_ALERTS_OFF));
                    else
                        sender.sendMessage(MessageUtil.translate(Config.Alerts.TOGGLE_ALERTS_ON));

                    break;

                case "profile":
                    if(args.length <= 1) {
                        sender.sendMessage("You must include a player");
                        return true;
                    }

                    final Player p = Bukkit.getPlayer(args[1]);
                    if(p == null) {
                        sender.sendMessage("Invalid player");
                        return true;
                    }

                    int movement = 0, combat = 0, player = 0;
                    FlappyPlayer data = FlappyAnticheat.INSTANCE.getDataManager().getData(p);

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

                    sender.sendMessage(
                            "Version: " + data.getClientVersion() +
                            "Brand: " + data.getClientBrand() +
                            "Total VL: " + (combat + player + movement) +
                            "Combat:" + combat +
                            "\nMovement: " + movement +
                            "\nPlayer:" + player);
                    break;
            }
        }
        return false;
    }
}