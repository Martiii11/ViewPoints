package me.martiii.viewpoints.commands;

import me.martiii.viewpoints.ViewPoints;
import me.martiii.viewpoints.viewpoint.ViewPoint;
import me.martiii.viewpoints.viewpoint.ViewPointType;
import net.minecraft.server.v1_11_R1.EntityArmorStand;
import net.minecraft.server.v1_11_R1.EntityPlayer;
import net.minecraft.server.v1_11_R1.PacketPlayOutCamera;
import net.minecraft.server.v1_11_R1.PacketPlayOutGameStateChange;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class ViewPointCmd implements CommandExecutor, Listener {
    private ViewPoints plugin;
    private HashMap<Player, PlayerInfo> playersInfo;

    public ViewPointCmd(ViewPoints plugin) {
        this.plugin = plugin;
        playersInfo = new HashMap<>();
        plugin.registerListener(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (!playersInfo.containsKey(player)){
                if (args.length >= 1){
                    String v = args[0];
                    if (plugin.getViewPointManager().viewPointExist(v)){
                        ViewPoint viewPoint = plugin.getViewPointManager().getViewPoint(v);
                        Location location = viewPoint.getLocation().clone();
                        Location oldPlayerLoc = player.getLocation().clone();
                        GameMode oldGamemode = player.getGameMode();
                        boolean oldAllowFlight = player.getAllowFlight();
                        boolean oldFlying = player.isFlying();
                        ItemStack[] oldContents = player.getInventory().getContents();
                        ItemStack[] oldArmor = player.getInventory().getArmorContents();

                        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000, 0, false, false));

                        //To prevent interact with self
                        player.teleport(location.add(1, 0, 0));

                        PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(3, 3);
                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

                        player.setAllowFlight(true);
                        player.setFlying(true);
                        player.getInventory().clear();
                        player.getInventory().setArmorContents(null);

                        //If chunk isn't loaded entity won't spawn.
                        location.getChunk().load();

                        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(viewPoint.getLocation(), EntityType.ARMOR_STAND);
                        armorStand.setVisible(false);
                        armorStand.setGravity(false);

                        if (viewPoint.getType().equals(ViewPointType.STATIC)) {
                            EntityArmorStand entity = ((CraftArmorStand) armorStand).getHandle();
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (!playersInfo.containsKey(player)) {
                                        cancel();
                                        return;
                                    }
                                    PacketPlayOutCamera packetCamera = new PacketPlayOutCamera(entity);
                                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetCamera);
                                }
                            }.runTaskTimer(plugin, 1, 5);
                        } else if (viewPoint.getType().equals(ViewPointType.MOBILE)){
                            armorStand.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(0);
                            armorStand.addPassenger(player);
                        }

                        playersInfo.put(player, new PlayerInfo(viewPoint.getType(), oldPlayerLoc, oldGamemode, oldFlying, oldAllowFlight, oldContents, oldArmor, armorStand));
                    } else {
                        player.sendMessage(ChatColor.RED + "That viewpoint doesn't exist.");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "/viewpoint <viewpoint>");
                }
            } else {
                player.sendMessage(ChatColor.RED + "You are already in a viewpoint.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "This command can only be used in game.");
        }
        return true;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        if (playersInfo.containsKey(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if (playersInfo.containsKey(player)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event){
        if (playersInfo.containsKey(event.getPlayer())){
            goBack(event.getPlayer());
        }
    }

    @EventHandler
    public void onShift(PlayerToggleSneakEvent event){
        if (playersInfo.containsKey(event.getPlayer()) && event.isSneaking()){
            goBack(event.getPlayer());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        if (playersInfo.containsKey(event.getPlayer())){
            goBack(event.getPlayer());
        }
    }

    private void goBack(Player player){
        PlayerInfo pi = playersInfo.remove(player);
        if (pi.getViewPointType().equals(ViewPointType.STATIC)) {
            EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
            PacketPlayOutCamera packetCamera = new PacketPlayOutCamera(entityPlayer);
            entityPlayer.playerConnection.sendPacket(packetCamera);
        } else if (pi.getViewPointType().equals(ViewPointType.MOBILE)){
            pi.getEntity().removePassenger(player);
            //pi.getLocation().getChunk().load();
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.teleport(pi.getLocation());
                }
            }.runTaskLater(plugin, 2L);
        }

        player.setGameMode(GameMode.SPECTATOR);
        player.setGameMode(pi.getGameMode());
        player.getInventory().setContents(pi.getInvContents());
        player.getInventory().setArmorContents(pi.getArmor());
        player.teleport(pi.getLocation());
        player.setAllowFlight(pi.wasAllowFlight());
        player.setFlying(pi.wasFlying());
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        pi.getEntity().remove();
    }

    public class PlayerInfo {
        private ViewPointType viewPointType;
        private Location location;
        private GameMode gameMode;
        private boolean flying;
        private boolean allowFlight;
        private ItemStack[] invContents;
        private ItemStack[] armor;
        private ArmorStand entity;

        PlayerInfo(ViewPointType viewPointType, Location location, GameMode gameMode, boolean flying, boolean allowFlight, ItemStack[] invContents, ItemStack[] armor, ArmorStand entity) {
            this.viewPointType = viewPointType;
            this.location = location;
            this.gameMode = gameMode;
            this.flying = flying;
            this.allowFlight = allowFlight;
            this.invContents = invContents;
            this.armor = armor;
            this.entity = entity;
        }

        public ViewPointType getViewPointType() {
            return viewPointType;
        }

        Location getLocation() {
            return location;
        }

        GameMode getGameMode() {
            return gameMode;
        }

        boolean wasFlying() {
            return flying;
        }

        boolean wasAllowFlight() {
            return allowFlight;
        }

        ItemStack[] getInvContents() {
            return invContents;
        }

        ItemStack[] getArmor() {
            return armor;
        }

        ArmorStand getEntity() {
            return entity;
        }
    }
}
