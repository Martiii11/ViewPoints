package me.martiii.viewpoints.commands;

import me.martiii.viewpoints.ViewPoints;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class ViewPointsTabCompleter implements TabCompleter{
    private ViewPoints plugin;

    public ViewPointsTabCompleter(ViewPoints plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1){
            List<String> list = new ArrayList<>();
            for (String v : plugin.getViewPointManager().getViewPointList()){
                if (v.toLowerCase().startsWith(args[0].toLowerCase())){
                    list.add(v);
                }
            }
            return list;
        }
        return null;
    }
}
