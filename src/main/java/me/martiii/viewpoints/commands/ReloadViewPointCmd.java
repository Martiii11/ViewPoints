package me.martiii.viewpoints.commands;

import me.martiii.viewpoints.ViewPoints;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadViewPointCmd implements CommandExecutor{
    private ViewPoints plugin;

    public ReloadViewPointCmd(ViewPoints plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.reloadConfig();
        plugin.getViewPointManager().loadConfig();
        sender.sendMessage(ChatColor.GREEN + "Config reloaded!");
        return true;
    }
}
