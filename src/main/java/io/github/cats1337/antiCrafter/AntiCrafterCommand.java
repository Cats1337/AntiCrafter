package io.github.cats1337.antiCrafter;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AntiCrafterCommand implements CommandExecutor {

    private final AntiCrafter plugin;

    public AntiCrafterCommand(final AntiCrafter pl) {
        this.plugin = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("anticrafter.reload")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        String wrongSyntax = ChatColor.translateAlternateColorCodes('&',
                plugin.getIncorrectSyntaxMessage()).replace("%command%", "/anticrafter reload");

        if (args.length != 1 || !args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(wrongSyntax);
            return true;
        }

        plugin.reloadConfig();
        plugin.updateConfig();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfigReloadedMessage()));

        return true;
    }
}
