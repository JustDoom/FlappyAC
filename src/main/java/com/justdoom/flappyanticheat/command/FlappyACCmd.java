package com.justdoom.flappyanticheat.command;

import com.imjustdoom.cmdinstruction.Command;
import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.command.subcommand.AlertsCmd;
import com.justdoom.flappyanticheat.command.subcommand.ReloadCmd;
import com.justdoom.flappyanticheat.command.subcommand.ResetViolationsCmd;
import com.justdoom.flappyanticheat.utils.Color;
import org.bukkit.command.CommandSender;

public class FlappyACCmd extends Command {

    public FlappyACCmd() {
        setSubcommands(
                new AlertsCmd().setName("alerts").setPermission("flappyanticheat.commands"),
                new ReloadCmd().setName("reload").setPermission("flappyanticheat.commands"),
                new ResetViolationsCmd().setName("resetviolations").setPermission("flappyanticheat.commands")
        );

        setTabCompletions("alerts", "reload", "resetviolations");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(Color.translate(FlappyAnticheat.getInstance().config.configuration.getString("prefix") + "&7Please do \"/flappyac help\" for the help command"));
    }
}
