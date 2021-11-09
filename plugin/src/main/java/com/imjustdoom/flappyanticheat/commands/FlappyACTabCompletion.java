package com.imjustdoom.flappyanticheat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class FlappyACTabCompletion implements TabCompleter {

    List<String> arguments = new ArrayList<>();
    List<String> violationArguments = new ArrayList<>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(arguments.isEmpty()){
            arguments.add("reload");
            arguments.add("violations");
            arguments.add("alerts");
            arguments.add("profile");
        }

        if(violationArguments.isEmpty()){
            violationArguments.add("reset");
        }

        if(args.length <= 1) {
            return arguments;
        }

        if(args[0].equalsIgnoreCase("violations")) {
            return violationArguments;
        }

        return null;
    }
}