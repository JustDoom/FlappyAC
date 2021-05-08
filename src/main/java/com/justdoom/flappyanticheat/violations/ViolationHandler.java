package com.justdoom.flappyanticheat.violations;

import com.justdoom.flappyanticheat.checks.Check;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ViolationHandler {

    private Map<UUID, Map<Check, Integer>> violations = new HashMap<>();

    public void addViolation(Check check, Player p){
        Map<Check, Integer> vl = new HashMap<>();
        if (this.violations.containsKey(p.getUniqueId())) {
            vl = this.violations.get(p.getUniqueId());
        }
        if (!vl.containsKey(check)) {
            vl.put(check, 1);
        } else {
            vl.put(check, vl.get(check) + 1);
        }
        this.violations.put(p.getUniqueId(), vl);
        //System.out.println(p.getName() + "  " + check + "  " + getViolations(check, p));
    }

    public Integer getViolations(Check check, Player p) {
        if (this.violations.containsKey(p.getUniqueId())) {
            if (this.violations.get(p.getUniqueId()).containsKey(check)) {
                return this.violations.get(p.getUniqueId()).get(check);
            }
        }
        return 0;
    }

    public void vlDecay(Check check, Player p){

    }

    public void clearViolations(Player p) {
        this.violations.remove(p.getUniqueId());
    }
}