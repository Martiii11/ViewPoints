package me.martiii.viewpoints.commands;

import me.martiii.viewpoints.ViewPoints;
import me.martiii.viewpoints.viewpoint.ViewPoint;
import me.martiii.viewpoints.viewpoint.ViewPointType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SetViewPointCmd implements CommandExecutor, TabCompleter{
    private ViewPoints plugin;
    private List<String> types;

    public SetViewPointCmd(ViewPoints plugin) {
        this.plugin = plugin;

        types = new ArrayList<>();
        types.add("static");
        types.add("mobile");
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (args.length >= 2){
                String v = args[0];
                String t = args[1].toUpperCase();
                if (!plugin.getViewPointManager().viewPointExist(v)){
                    if (t.equals("STATIC") || t.equals("MOBILE")) {
                        plugin.getViewPointManager().addViewPoint(new ViewPoint(v, player.getLocation(), ViewPointType.valueOf(t)));
                        player.sendMessage(ChatColor.GREEN + "Viewpoint " + v + " set!");
                    } else {
                        player.sendMessage(ChatColor.RED + "/setviewpoint " + v + " <static/mobile>");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "That viewpoint already exists.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "/setviewpoint <viewpoint> <static/mobile>");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "This command can only be used in game.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 2){
            return types;
        }
        return Collections.emptyList();
    }
}
