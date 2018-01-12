package me.martiii.viewpoints.viewpoint;

import me.martiii.viewpoints.ViewPoints;
import me.martiii.viewpoints.utils.LocationSerializer;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class ViewPointManager {
    private HashMap<String, ViewPoint> viewpoints;
    private ViewPoints plugin;

    public ViewPointManager(ViewPoints plugin){
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig(){
        viewpoints = new HashMap<>();
        FileConfiguration config = plugin.getConfig();
        for (String viewpoint : config.getConfigurationSection("viewpoints").getKeys(false)){
            Location loc = LocationSerializer.toLocation(config.getString("viewpoints." + viewpoint));
            viewpoints.put(viewpoint, new ViewPoint(viewpoint, loc));
        }
    }

    public boolean viewPointExist(String viewpoint){
        return viewpoints.containsKey(viewpoint);
    }

    public ViewPoint getViewPoint(String viewpoint){
        return viewpoints.get(viewpoint);
    }

    public void addViewPoint(ViewPoint viewpoint){
        viewpoints.put(viewpoint.getName(), viewpoint);
        plugin.getConfig().set("viewpoints." + viewpoint.getName(), LocationSerializer.toString(viewpoint.getLocation()));
        plugin.saveConfig();
    }

    public void removeViewPoint(String viewpoint){
        viewpoints.remove(viewpoint);
        plugin.getConfig().set("viewpoints." + viewpoint, null);
        plugin.saveConfig();
    }
}
