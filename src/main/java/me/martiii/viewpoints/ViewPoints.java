package me.martiii.viewpoints;

import me.martiii.viewpoints.commands.DelViewPointCmd;
import me.martiii.viewpoints.commands.ReloadViewPointCmd;
import me.martiii.viewpoints.commands.SetViewPointCmd;
import me.martiii.viewpoints.commands.ViewPointCmd;
import me.martiii.viewpoints.viewpoint.ViewPointManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class ViewPoints extends JavaPlugin {
    private ViewPointManager viewPointManager;

    @Override
    public void onEnable() {
        super.onEnable();

        saveDefaultConfig();

        viewPointManager = new ViewPointManager(this);

        getCommand("viewpoint").setExecutor(new ViewPointCmd(this));
        getCommand("setviewpoint").setExecutor(new SetViewPointCmd(this));
        getCommand("delviewpoint").setExecutor(new DelViewPointCmd(this));
        getCommand("viewpointreload").setExecutor(new ReloadViewPointCmd(this));
    }

    public ViewPointManager getViewPointManager() {
        return viewPointManager;
    }

    public void registerListener(Listener listener){
        getServer().getPluginManager().registerEvents(listener, this);
    }
}
