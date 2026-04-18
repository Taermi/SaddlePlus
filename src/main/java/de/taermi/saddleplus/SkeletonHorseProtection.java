package de.taermi.saddleplus;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataType;

//No HashMaps anymnore, way smaller and still works fine

public class SkeletonHorseProtection implements Listener {

    private final NamespacedKey ownerKey;

    public SkeletonHorseProtection(NamespacedKey ownerKey) {
        this.ownerKey = ownerKey;
    }

    @EventHandler
    public void onHorseDamageBySkeletonArrow(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof AbstractHorse horse)) return;
        if (!(event.getDamager() instanceof Arrow arrow)) return;
        if (!(arrow.getShooter() instanceof AbstractSkeleton skeleton)) return;

        // get direct target of the skeleton
        if (!(skeleton.getTarget() instanceof Player targetPlayer)) return;

        if (horse.getInventory().getSaddle() == null) return;
        if (horse.getInventory().getSaddle().getItemMeta() == null) return;

        String owner = horse.getInventory().getSaddle().getItemMeta()
                .getPersistentDataContainer()
                .get(ownerKey, PersistentDataType.STRING);

        if (owner != null && !owner.isEmpty() && !owner.equals(targetPlayer.getName())) {
            event.setCancelled(true);
        }
    }
}