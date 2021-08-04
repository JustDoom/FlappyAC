package com.justdoom.flappyanticheat.checks;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.exempt.type.ExemptType;
import com.justdoom.flappyanticheat.packet.Packet;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Setter
public abstract class Check {

    public String check, checkType, description;
    public boolean experimental;
    private int maxVl, vl;

    public FlappyPlayer player;
    public CheckInfo checkData;

    public Check(FlappyPlayer player) {
        this.player = player;

        this.checkData = getClass().getAnnotation(CheckInfo.class);

        this.check = checkData.check();
        this.checkType = checkData.checkType();
        this.experimental = checkData.experimental();
        this.description = checkData.description();

        loadConfigOptions();
    }

    public abstract void handle(final Packet packet);

    protected boolean isExempt(final ExemptType exemptType) {
        return player.getExemptManager().isExempt(exemptType);
    }

    protected boolean isExempt(final ExemptType... exemptTypes) {
        return player.getExemptManager().isExempt(exemptTypes);
    }

    public void fail(String info){

        vl++;

        String msg = "You are hacking " + check + " " + checkType + " || " + vl + "/" + maxVl;

        TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', msg));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', description + "\n" + info)).create()));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/say clicked hehehehheheheheheheh"));

        Executors.newSingleThreadExecutor().execute(() -> Bukkit.getOnlinePlayers()
                .stream()
                .filter(send -> send.hasPermission("flappyac.alerts"))
                .forEach(send -> send.spigot().sendMessage(component)));

        if(vl >= maxVl){
            //TODO: punish
        }
    }

    public void loadConfigOptions(){
        ConfigurationNode config = FlappyAnticheat.INSTANCE.getConfigFile();
        maxVl = config.node("checks." + check.toLowerCase() + "." + checkType.toLowerCase() + ".punish=vl").getInt();
    }

    public void sync(Runnable runnable) {
        AtomicBoolean waiting = new AtomicBoolean(true);
        if (FlappyAnticheat.INSTANCE.getPlugin().isEnabled()) {
            Bukkit.getScheduler().runTask(FlappyAnticheat.INSTANCE.getPlugin(), () -> {
                runnable.run();
                waiting.set(false);
            });
        }
        while (waiting.get()) {
        }
    }
}