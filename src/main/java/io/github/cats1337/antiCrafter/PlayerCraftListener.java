package io.github.cats1337.antiCrafter;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.inventory.CraftItemEvent;

import java.util.HashSet;
import java.util.Set;

public class PlayerCraftListener implements Listener {

    private final AntiCrafter plugin;
    private final Set<Material> blockedMaterials = new HashSet<>();

    public PlayerCraftListener(final AntiCrafter pl) {
        this.plugin = pl;
        loadBlockedMaterials();
    }

    private void loadBlockedMaterials() {
        for (String mat : plugin.getDefaultBlockList()) {
            try {
                blockedMaterials.add(Material.valueOf(mat));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid material in config: " + mat + ". Check Spigot's Material list.");
            }
        }
    }

    @EventHandler
    public void onPlayerCraft(CraftItemEvent event) {
        if (event.getWhoClicked().hasPermission("anticrafter.bypass")) return;
        if (!plugin.isLCEnabled()) return;

        Material craftedItem = event.getCurrentItem().getType();
        String msg = ChatColor.translateAlternateColorCodes('&', plugin.getNotPermittedToCraftMessage())
                .replace("%item%", craftedItem.name().toLowerCase());

        if (plugin.isBlockAllItemsEnabled() || blockedMaterials.contains(craftedItem)) {
            event.setCancelled(true);
            event.getWhoClicked().sendMessage(msg);
            return;
        }

        if (plugin.isGroupsEnabled()) {
            for (String group : plugin.getGroupNames()) {
                if (event.getWhoClicked().hasPermission("anticrafter.groups." + group)) {
                    Set<Material> groupMaterials = new HashSet<>();
                    for (String mat : plugin.getGroupMaterials(group)) {
                        try {
                            groupMaterials.add(Material.valueOf(mat));
                        } catch (IllegalArgumentException e) {
                            plugin.getLogger().warning("Invalid material in group " + group + ": " + mat);
                        }
                    }
                    if (groupMaterials.contains(craftedItem)) {
                        event.setCancelled(true);
                        event.getWhoClicked().sendMessage(msg);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCrafterCraft(CrafterCraftEvent event) {
        if (!plugin.isLCEnabled()) return;

        Material craftedItem = event.getRecipe().getResult().getType();

        if (plugin.isBlockAllItemsEnabled() || blockedMaterials.contains(craftedItem)) {
            event.setCancelled(true);
            plugin.getLogger().info("Auto-crafter attempted to craft a blocked item: " + craftedItem.name());
            return;
        }

        if (plugin.isGroupsEnabled()) {
            for (String group : plugin.getGroupNames()) {
                Set<Material> groupMaterials = new HashSet<>();
                for (String mat : plugin.getGroupMaterials(group)) {
                    try {
                        groupMaterials.add(Material.valueOf(mat));
                    } catch (IllegalArgumentException e) {
                        plugin.getLogger().warning("Invalid material in group " + group + ": " + mat);
                    }
                }
                if (groupMaterials.contains(craftedItem)) {
                    event.setCancelled(true);
                    plugin.getLogger().info("Auto-crafter attempted to craft a group-blocked item: " + craftedItem.name());
                    return;
                }
            }
        }
    }
}
