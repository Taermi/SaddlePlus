package de.taermi.saddleplus;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.persistence.PersistentDataType;

public class CreeperHorseProtection implements Listener {

    private final NamespacedKey ownerKey;
    private Player creeperTargetPlayer = null; // track creepers target player

    public CreeperHorseProtection(JavaPlugin plugin) {
        this.ownerKey = new NamespacedKey(plugin, "saddleOwner");
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onCreeperTargetPlayer(EntityTargetEvent event) {
        // check if creeper targets player
        if (event.getEntity() instanceof Creeper && event.getTarget() instanceof Player) {
            Creeper creeper = (Creeper) event.getEntity();
            Player player = (Player) event.getTarget();

            // saves creeper targetplayer
            this.creeperTargetPlayer = player;
        }
    }

    @EventHandler
    public void onHorseDamageByCreeper(EntityDamageByEntityEvent event) {
        Entity damagedEntity = event.getEntity();

        // check for horse
        if (damagedEntity instanceof AbstractHorse) {
            AbstractHorse horse = (AbstractHorse) damagedEntity;

            // check if damage by creeper explosion
            if (event.getDamager() instanceof Creeper) {
                Creeper creeper = (Creeper) event.getDamager();

                if (this.creeperTargetPlayer != null) {
                    // get horse owner
                    if (horse.getInventory().getSaddle() != null && horse.getInventory().getSaddle().getItemMeta() != null) {
                        String owner = horse.getInventory().getSaddle().getItemMeta()
                                .getPersistentDataContainer()
                                .get(ownerKey, PersistentDataType.STRING);

                        // prevent damage if entity horse
                        if (owner != null && !owner.isEmpty() && !owner.equals(creeperTargetPlayer.getName())) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCreeperExplosion(EntityExplodeEvent event) {
        Entity creeper = event.getEntity();

        // If a creeper explodes, clear the target player, because the creeper no longer exists
        if (creeper instanceof Creeper) {
            creeperTargetPlayer = null;
        }
    }
}
