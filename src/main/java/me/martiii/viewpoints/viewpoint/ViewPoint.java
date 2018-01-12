package me.martiii.viewpoints.viewpoint;

import org.bukkit.Location;

public class ViewPoint {
    private String name;
    private Location location;

    public ViewPoint(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }
}
