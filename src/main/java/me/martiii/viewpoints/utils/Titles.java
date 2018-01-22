package me.martiii.viewpoints.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class Titles {
    public static boolean oldReflection = false;
    public static boolean newReflection = false;

    public static void sendHotbarPacket(Player player, String message) {
        try {
            Object packet;
            if (oldReflection) {
                Constructor<?> constructor = getNMSClass("PacketPlayOutChat")
                        .getConstructor(getNMSClass("IChatBaseComponent"), byte.class);
                Object m = getNMSClass("ChatSerializer").getDeclaredMethod("a", String.class).invoke(null, message);
                packet = constructor.newInstance(m, (byte) 2);
            } else if (newReflection) {
                Constructor<?> constructor = getNMSClass("PacketPlayOutChat")
                        .getConstructor(getNMSClass("IChatBaseComponent"), getNMSClass("ChatMessageType"));
                Object enumType = getNMSClass("ChatMessageType").getField("GAME_INFO").get(null);
                Object m = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getDeclaredMethod("a", String.class)
                        .invoke(null, message);
                packet = constructor.newInstance(m, enumType);
            } else {
                Constructor<?> constructor = getNMSClass("PacketPlayOutChat")
                        .getConstructor(getNMSClass("IChatBaseComponent"), byte.class);
                Object m = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getDeclaredMethod("a", String.class)
                        .invoke(null, message);
                packet = constructor.newInstance(m, (byte) 2);
            }
            sendPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerCon = handle.getClass().getField("playerConnection").get(handle);
            playerCon.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerCon, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Class<?> getNMSClass(String name) {
        String v = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + v + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toJSON(String text) {
        return "{\"text\": \"" + text + "\"}";
    }
}
