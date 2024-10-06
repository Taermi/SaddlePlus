package de.taermi.saddleplus;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.WeakHashMap;

public class CreeperHorseProtection implements Listener {

    private final NamespacedKey ownerKey;
    // map to store target player for each creeper
    private final Map<Creeper, Player> creeperTargetMap = new WeakHashMap<>();

    public CreeperHorseProtection(NamespacedKey ownerKey) {
        this.ownerKey = ownerKey;
        Bukkit.getPluginManager().registerEvents(this, Bukkit.getPluginManager().getPlugin("SaddlePlus"));
    }

    @EventHandler
    public void onCreeperTargetPlayer(EntityTargetEvent event) {
        // check if creeper targets player
        if (event.getEntity() instanceof Creeper && event.getTarget() instanceof Player) {
            Creeper creeper = (Creeper) event.getEntity();
            Player player = (Player) event.getTarget();
            // save creeper target player in the map
            creeperTargetMap.put(creeper, player);
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
                // get the target player for this creeper from the map
                Player targetPlayer = creeperTargetMap.get(creeper);
                if (targetPlayer != null) {
                    // get horse owner
                    if (horse.getInventory().getSaddle() != null && horse.getInventory().getSaddle().getItemMeta() != null) {
                        String owner = horse.getInventory().getSaddle().getItemMeta()
                                .getPersistentDataContainer()
                                .get(ownerKey, PersistentDataType.STRING);
                        // prevent damage if the horse owner is not the creepers target player
                        if (owner != null && !owner.isEmpty() && !owner.equals(targetPlayer.getName())) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}