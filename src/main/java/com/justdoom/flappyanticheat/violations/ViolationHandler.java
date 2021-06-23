package com.justdoom.flappyanticheat.violations;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.customevents.PunishEvent;
import com.justdoom.flappyanticheat.customevents.ViolationResetEvent;
import com.justdoom.flappyanticheat.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class ViolationHandler {

    private Map<UUID, Map<Check, Integer>> violations = new HashMap<>();

    public ViolationHandler(){
        int delay = FlappyAnticheat.getInstance().getConfig().getInt("violation-reset-delay") * 20;

        Bukkit.getScheduler().runTaskTimerAsynchronously(FlappyAnticheat.getInstance(), () -> {
            ViolationResetEvent violationResetEvent = new ViolationResetEvent();
            Bukkit.getPluginManager().callEvent(violationResetEvent);
            if(!violationResetEvent.isCancelled() && Bukkit.getOnlinePlayers().size() > 0) {
                clearAllViolations();
                for(Player p: Bukkit.getOnlinePlayers()){
                    if(p.hasPermission("flappyanticheat.alerts")){
                        p.sendMessage(Color.translate(FlappyAnticheat.getInstance().getConfig().getString("prefix") + FlappyAnticheat.getInstance().getConfig().getString("messages.violation-reset.all")));
                    }
                }
                if(FlappyAnticheat.getInstance().getConfig().getBoolean("messages.flag-to-console")) {
                    Bukkit.getConsoleSender().sendMessage(Color.translate(FlappyAnticheat.getInstance().getConfig().getString("prefix") + FlappyAnticheat.getInstance().getConfig().getString("messages.violation-reset.all")));
                }
            }
        }, delay, delay);
    }

    public void addViolation(Check check, Player p){
        String path = "checks." + check.check.toLowerCase() + "." + check.checkType.toLowerCase();

        if(!FlappyAnticheat.getInstance().getConfig().getBoolean(path + ".enabled")){
            return;
        }

        int violation = FlappyAnticheat.getInstance().getConfig().getInt( path + ".vl");
        Map<Check, Integer> vl = new HashMap<>();
        if (this.violations.containsKey(p.getUniqueId())) {
            vl = this.violations.get(p.getUniqueId());
        }
        if (!vl.containsKey(check)) {
            vl.put(check, violation);
        } else {
            vl.put(check, vl.get(check) + violation);
        }
        this.violations.put(p.getUniqueId(), vl);
        if(getViolations(check, p) >= FlappyAnticheat.getInstance().getConfig().getInt(path + ".punish-vl")){
            if(FlappyAnticheat.getInstance().getConfig().getBoolean(path + ".punishable")){
                check.punish(p, path);
                FlappyAnticheat.getInstance().fileData.addToFile("punishments.txt", "\n" + p.getName() + " has been punished for " + check.check.toLowerCase() + " " + check.checkType.toLowerCase());
            }
        }
    }

    public Integer getViolations(Check check, Player p) {
        if (this.violations.containsKey(p.getUniqueId())) {
            if (this.violations.get(p.getUniqueId()).containsKey(check)) {
                return this.violations.get(p.getUniqueId()).get(check);
            }
        }
        return 0;
    }

    public void clearViolations(Player p) {
        this.violations.remove(p.getUniqueId());
    }

    public void clearAllViolations() {
        this.violations.clear();
    }
}