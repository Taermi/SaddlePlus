package de.taermi.saddleplus;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class SaddleCommandTrigger implements Listener {
    private final JavaPlugin plugin;
    private final NamespacedKey ownerKey;

    public SaddleCommandTrigger(JavaPlugin plugin) {
        this.plugin = plugin;
        this.ownerKey = new NamespacedKey(plugin, "saddleOwner");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // check for sneaking
        if (event.getPlayer().isSneaking()) {
            // check for right click
            if (event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_AIR ||
                    event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) {

                Player player = event.getPlayer();
                ItemStack item = player.getInventory().getItemInMainHand();

                // check for saddle in main hand
                if (item.getType() == Material.SADDLE) {
                    ItemMeta meta = item.getItemMeta();

                    if (meta != null) {
                        // check if saddle has owner
                        if (meta.getPersistentDataContainer().has(ownerKey, PersistentDataType.STRING)) {
                            String owner = meta.getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING);

                            // if player = owner -> unclaim
                            if (owner.equals(player.getName())) {
                                player.performCommand("saddle unclaim");
                            } else {
                                Utils.sendActionbarMSG(player, "You cannot unclaim a saddle that is not yours.", net.md_5.bungee.api.ChatColor.RED);
                            }
                        } else {
                            // no owner -> claim
                            player.performCommand("saddle claim");
                        }
                    }
                }
            }
        }
    }
}
