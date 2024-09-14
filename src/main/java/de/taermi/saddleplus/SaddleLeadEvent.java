package de.taermi.saddleplus;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class SaddleLeadEvent implements Listener {

    private final NamespacedKey ownerKey;

    public SaddleLeadEvent(JavaPlugin plugin) {
        this.ownerKey = new NamespacedKey(plugin, "saddleOwner");
    }

    @EventHandler
    public void onPlayerLeashEntity(PlayerLeashEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getEntity();

        // check for saddle
        ItemStack saddle = getSaddle(entity);
        if (saddle != null) {
            ItemMeta meta = saddle.getItemMeta();
            if (meta != null && meta.getPersistentDataContainer().has(ownerKey, PersistentDataType.STRING)) {
                String owner = meta.getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING);

                // check if owner
                if (!owner.equals(player.getName())) {
                    event.setCancelled(true);  // block leash
                    player.sendMessage(ChatColor.RED + "You cannot leash other player animals.");
                }
            }
        }
    }
    // method to call the saddle of the entity
    private ItemStack getSaddle(Entity entity) {
        if (entity instanceof AbstractHorse horse)
            return horse.getInventory().getSaddle();
        return null;
    }
}