package de.taermi.saddleplus;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;

public class SaddleGriefPlace implements Listener {

    private final NamespacedKey ownerKey;

    public SaddleGriefPlace(NamespacedKey ownerKey) {
        this.ownerKey = ownerKey;
        Bukkit.getPluginManager().registerEvents(this, Bukkit.getPluginManager().getPlugin("SaddlePlus"));
    }

    // BlockPlaceEvent for fire placement
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Material placedBlock = event.getBlock().getType();

        // check if placed block is fire (flint and steel, fire charge etc.)
        if (placedBlock == Material.FIRE || placedBlock == Material.FIRE_CORAL_BLOCK) {
            checkNearbyEntities(event.getBlock().getLocation().toVector(), player, event);
        }
    }

    // PlayerInteractEvent for lava buckets
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();

        // check if player is using lava bucket
        if (action == Action.RIGHT_CLICK_BLOCK && item != null && item.getType() == Material.LAVA_BUCKET) {
            Block clickedBlock = event.getClickedBlock();
            if (clickedBlock != null) {
                checkNearbyEntities(clickedBlock.getLocation().toVector(), player, event);
            }
        }
    }

    // method to check distance to horse
    private void checkNearbyEntities(Vector location, Player player, Cancellable event) {
        // check all entities in reach of 3 blocks
        for (Entity entity : player.getWorld().getNearbyEntities(location.toLocation(player.getWorld()), 3, 3, 3)) {
            // check for horse
            if (entity instanceof AbstractHorse horse) {
                // check for saddle
                ItemStack saddle = horse.getInventory().getSaddle();
                if (saddle != null && saddle.getItemMeta() != null) {
                    ItemMeta meta = saddle.getItemMeta();
                    if (meta.getPersistentDataContainer().has(ownerKey, PersistentDataType.STRING)) {
                        String owner = meta.getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING);
                        // check if attacker isn't the owner
                        if (!owner.equals(player.getName())) {
                            // prevent lava and fire placing
                            event.setCancelled(true);
                            Utils.sendActionbarMSG(player, "You can't place lava or fire near someone else's animal!", ChatColor.RED);
                        }
                    }
                }
            }
        }
    }
}