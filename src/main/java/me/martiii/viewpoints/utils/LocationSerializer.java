package me.martiii.viewpoints.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationSerializer {

    public static String toString(Location loc){
        //Convert location with pitch and yaw to simple string
        return loc.getWorld().getName() + ", " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ", " + loc.getYaw() + ", " + loc.getPitch();
    }

    public static Location toLocation(String loc){
        //Convert simple string to location with pitch and yaw
        String[] parts = loc.split(", ");
        World world = Bukkit.getServer().getWorld(parts[0]);
        double x = Double.valueOf(parts[1]);
        double y = Double.valueOf(parts[2]);
        double z = Double.valueOf(parts[3]);
        float yaw = Float.valueOf(parts[4]);
        float pitch = Float.valueOf(parts[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }
}
