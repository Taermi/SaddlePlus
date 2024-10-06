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

public class SkeletonHorseProtection implements Listener {

    private final NamespacedKey ownerKey;
    // map to store target player for each skeleton
    private final Map<AbstractSkeleton, Player> skeletonTargetMap = new WeakHashMap<>();

    public SkeletonHorseProtection(NamespacedKey ownerKey) {
        this.ownerKey = ownerKey;
        Bukkit.getPluginManager().registerEvents(this, Bukkit.getPluginManager().getPlugin("SaddlePlus"));
    }

    @EventHandler
    public void onSkeletonTargetPlayer(EntityTargetEvent event) {
        // check if a skeleton targets a player
        if (event.getEntity() instanceof AbstractSkeleton && event.getTarget() instanceof Player) {
            AbstractSkeleton skeleton = (AbstractSkeleton) event.getEntity();
            Player player = (Player) event.getTarget();

            // store the skeletons target player in the map
            skeletonTargetMap.put(skeleton, player);
        }
    }

    @EventHandler
    public void onHorseDamageBySkeletonArrow(EntityDamageByEntityEvent event) {
        Entity damagedEntity = event.getEntity();

        // check if the damaged entity is a horse/donkey/mule
        if (damagedEntity instanceof AbstractHorse) {
            AbstractHorse horse = (AbstractHorse) damagedEntity;

            // check if the damage is caused by an arrow
            if (event.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getDamager();

                // check if the arrow is shot by a skeleton
                if (arrow.getShooter() instanceof AbstractSkeleton) {
                    AbstractSkeleton skeleton = (AbstractSkeleton) arrow.getShooter();

                    // get the target player for this skeleton from the map
                    Player targetPlayer = skeletonTargetMap.get(skeleton);

                    if (targetPlayer != null) {
                        if (horse.getInventory().getSaddle() != null && horse.getInventory().getSaddle().getItemMeta() != null) {
                            String owner = horse.getInventory().getSaddle().getItemMeta()
                                    .getPersistentDataContainer()
                                    .get(ownerKey, PersistentDataType.STRING);

                            // if the horse has an owner and its not the skeletons target, prevent damage
                            if (owner != null && !owner.isEmpty() && !owner.equals(targetPlayer.getName())) {
                                //targetPlayer.sendMessage(skeletonTargetMap.toString()); //test if the WeakHashMap deletes dead/unloaded enemys
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }
}
