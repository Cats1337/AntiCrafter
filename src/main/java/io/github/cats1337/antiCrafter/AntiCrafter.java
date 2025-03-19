package io.github.cats1337.antiCrafter;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Set;

public class AntiCrafter extends JavaPlugin {

    private boolean enabled;
    private String configReloaded;
    private String incorrectSyntax;
    private String message;
    private boolean blockAllItems;
    private List<String> materialList;
    private Boolean enableGroups;
    private Set<String> groupNames;

    @Override
    public void onEnable() {
        updateConfig();
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new PlayerCraftListener(this), this);

        if (getCommand("anticrafter") == null) {
            getLogger().severe("Command 'anticrafter' is missing from plugin.yml!");
            return;
        }

        getCommand("anticrafter").setExecutor(new AntiCrafterCommand(this));
    }


    public void updateConfig() {
        enabled = getConfig().getBoolean("anticrafter.enabled");
        configReloaded = getConfig().getString("anticrafter.config-reloaded");
        incorrectSyntax = getConfig().getString("anticrafter.incorrect-syntax");
        message = getConfig().getString("anticrafter.message");
        blockAllItems = getConfig().getBoolean("anticrafter.block-all-items");
        materialList = getConfig().getStringList("anticrafter.default-block-list");
        enableGroups = getConfig().getBoolean("anticrafter.enable-groups");
        groupNames = getConfig().getConfigurationSection("anticrafter.groups").getKeys(false);
    }

    public boolean isLCEnabled() {
        return enabled;
    }

    public String getConfigReloadedMessage() {
        return configReloaded;
    }

    public String getIncorrectSyntaxMessage() {
        return incorrectSyntax;
    }

    public String getNotPermittedToCraftMessage() {
        return message;
    }

    public boolean isBlockAllItemsEnabled() {
        return blockAllItems;
    }

    public List<String> getDefaultBlockList() {
        return materialList;
    }

    public boolean isGroupsEnabled() {
        return enableGroups;
    }

    public Set<String> getGroupNames() {
        return groupNames;
    }

    public List<String> getGroupMaterials(String groupName) {
        return getConfig().getStringList("anticrafter.groups." + groupName);
    }



}