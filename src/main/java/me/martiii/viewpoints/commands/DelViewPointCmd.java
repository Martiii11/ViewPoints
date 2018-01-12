package me.martiii.viewpoints.commands;

import me.martiii.viewpoints.ViewPoints;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelViewPointCmd implements CommandExecutor{
    private ViewPoints plugin;

    public DelViewPointCmd(ViewPoints plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (args.length >= 1){
                String v = args[0];
                if (plugin.getViewPointManager().viewPointExist(v)){
                    plugin.getViewPointManager().removeViewPoint(v);
                    player.sendMessage(ChatColor.GREEN + "Viewpoint " + v + " deleted!");
                } else {
                    player.sendMessage(ChatColor.RED + "That viewpoint doesn't exist.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "/delviewpoint <viewpoint>");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "This command can only be used in game.");
        }
        return true;
    }
}
