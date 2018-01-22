package me.martiii.viewpoints.viewpoint;

import me.martiii.viewpoints.ViewPoints;
import me.martiii.viewpoints.utils.LocationSerializer;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Set;

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
        //Convert file config from old version
        if (config.contains("config-version")){
            if (config.getDouble("config-version") == 1.0){
                if (config.contains("viewpoints")) {
                    for (String viewpoint : config.getConfigurationSection("viewpoints").getKeys(false)) {
                        String loc = config.getString("viewpoints." + viewpoint);
                        plugin.getConfig().set("viewpoints." + viewpoint, null);
                        plugin.getConfig().set("viewpoints." + viewpoint + ".loc", loc);
                        plugin.getConfig().set("viewpoints." + viewpoint + ".type", ViewPointType.STATIC.toString());
                    }
                }
                plugin.getConfig().set("config-version", 2.0);
                plugin.saveConfig();
            }
        }
        if (config.contains("viewpoints")) {
            for (String viewpoint : config.getConfigurationSection("viewpoints").getKeys(false)) {
                Location loc = LocationSerializer.toLocation(config.getString("viewpoints." + viewpoint + ".loc"));
                viewpoints.put(viewpoint, new ViewPoint(viewpoint, loc, ViewPointType.valueOf(config.getString("viewpoints." + viewpoint + ".type"))));
            }
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
        plugin.getConfig().set("viewpoints." + viewpoint.getName() + ".loc", LocationSerializer.toString(viewpoint.getLocation()));
        plugin.getConfig().set("viewpoints." + viewpoint.getName() + ".type", viewpoint.getType().toString());
        plugin.saveConfig();
    }

    public void removeViewPoint(String viewpoint){
        viewpoints.remove(viewpoint);
        plugin.getConfig().set("viewpoints." + viewpoint, null);
        plugin.saveConfig();
    }

    public Set<String> getViewPointList(){
        return viewpoints.keySet();
    }
}
