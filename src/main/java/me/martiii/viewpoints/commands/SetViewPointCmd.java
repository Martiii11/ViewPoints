package me.martiii.viewpoints.commands;

import me.martiii.viewpoints.ViewPoints;
import me.martiii.viewpoints.viewpoint.ViewPoint;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetViewPointCmd implements CommandExecutor{
    private ViewPoints plugin;

    public SetViewPointCmd(ViewPoints plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (args.length >= 1){
                String v = args[0];
                if (!plugin.getViewPointManager().viewPointExist(v)){
                    plugin.getViewPointManager().addViewPoint(new ViewPoint(v, player.getLocation()));
                    player.sendMessage(ChatColor.GREEN + "Viewpoint " + v + " set!");
                } else {
                    player.sendMessage(ChatColor.RED + "That view point already exists.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "/setviewpoint <viewpoint>");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "This command can only be used in game.");
        }
        return true;
    }
}
