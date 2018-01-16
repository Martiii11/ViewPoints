package me.martiii.viewpoints.viewpoint;

import org.bukkit.Location;

public class ViewPoint {
    private String name;
    private Location location;
    private ViewPointType type;

    public ViewPoint(String name, Location location, ViewPointType type) {
        this.name = name;
        this.location = location;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public ViewPointType getType() {
        return type;
    }
}
