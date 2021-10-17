package com.imjustdoom.flappyanticheat.menu;

import com.imjustdoom.api.check.FlappyCheck;
import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.config.Config;
import com.imjustdoom.flappyanticheat.manager.CheckManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.spongepowered.configurate.ConfigurationNode;

public class MenuGUI {

    private Inventory menu;

    public MenuGUI() {
        menu = Bukkit.createInventory(null, 54, Config.Menu.NAME);
    }

    public void load(final Player player) {
        int slot = 0;
        for (final FlappyCheck check : FlappyAnticheat.INSTANCE.getDataManager().getData(player).getChecks()) {
            final Material material = check.isEnabled() ? Material.GREEN_STAINED_GLASS : Material.RED_STAINED_GLASS;

            ItemStack itemStack = new ItemStack(material);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(check.getCheckInfo().check() + " " + check.getCheckInfo().checkType());

            itemStack.setItemMeta(itemMeta);

            menu.setItem(slot++, itemStack);
        }

        player.openInventory(menu);
    }
}
