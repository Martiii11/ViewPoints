package me.martiii.viewpoints;

import me.martiii.viewpoints.commands.*;
import me.martiii.viewpoints.utils.Titles;
import me.martiii.viewpoints.viewpoint.ViewPointManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class ViewPoints extends JavaPlugin {
    private ViewPointManager viewPointManager;
    private ViewPointCmd viewPointCmd;

    @Override
    public void onEnable() {
        super.onEnable();

        //Minecraft titles for 1.8 and 1.12 have different packets
        String v = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        Titles.oldReflection = v.equals("v1_8_R1");
        Titles.newReflection = v.contains("v1_12");

        saveDefaultConfig();

        viewPointManager = new ViewPointManager(this);

        ViewPointsTabCompleter viewPointsTabCompleter = new ViewPointsTabCompleter(this);

        viewPointCmd = new ViewPointCmd(this);
        getCommand("viewpoint").setExecutor(viewPointCmd);
        getCommand("viewpoint").setTabCompleter(viewPointsTabCompleter);

        SetViewPointCmd setViewPointCmd = new SetViewPointCmd(this);
        getCommand("setviewpoint").setExecutor(setViewPointCmd);
        getCommand("setviewpoint").setTabCompleter(setViewPointCmd);

        DelViewPointCmd delViewPointCmd = new DelViewPointCmd(this);
        getCommand("delviewpoint").setExecutor(delViewPointCmd);
        getCommand("delviewpoint").setTabCompleter(viewPointsTabCompleter);

        ReloadViewPointCmd reloadViewPointCmd = new ReloadViewPointCmd(this);
        getCommand("viewpointreload").setExecutor(reloadViewPointCmd);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        viewPointCmd.goBackAll();
    }

    public ViewPointManager getViewPointManager() {
        return viewPointManager;
    }

    public void registerListener(Listener listener){
        getServer().getPluginManager().registerEvents(listener, this);
    }
}
