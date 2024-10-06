package de.taermi.saddleplus;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class SaddleCommand implements CommandExecutor {

    private final NamespacedKey ownerKey;

    public SaddleCommand(JavaPlugin plugin) {
        this.ownerKey = new NamespacedKey(plugin, "saddleOwner");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players."); //console only, so this isn't an action bar message
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("claim")) {
                claimSaddle(player);
                return true;
            } else if (args[0].equalsIgnoreCase("unclaim")) {
                unclaimSaddle(player);
                return true;
            }
        }

        Utils.sendActionbarMSG(player, "Usage: /saddle <claim | unclaim>", ChatColor.RED);
        return false;
    }

    private boolean claimSaddle(Player player) {
        // check if player has a saddle in hand
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem.getType() != Material.SADDLE) {
            Utils.sendActionbarMSG(player, "You must hold a saddle to claim it.", ChatColor.RED);
            return true;
        }

        ItemMeta meta = heldItem.getItemMeta();

        if (meta != null) {
            // check if saddle already has a owner
            if (meta.getPersistentDataContainer().has(ownerKey, PersistentDataType.STRING)) {
                String owner = meta.getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING);
                // player can only claim if the saddle do not have an owner:
                if (!owner.isEmpty()) {
                    Utils.sendActionbarMSG(player, "This saddle is already claimed by " + owner + ".", ChatColor.RED);
                    return true;
                }
            }

            // else we can claim the saddle:
            meta.getPersistentDataContainer().set(ownerKey, PersistentDataType.STRING, player.getName());
            meta.setDisplayName(ChatColor.GOLD + player.getName() + "'s Saddle"); // set saddle name
            heldItem.setItemMeta(meta);
            Utils.sendActionbarMSG(player, "You have claimed the saddle.", ChatColor.GREEN);
            return true;
        }
        return false;
    }

    private boolean unclaimSaddle(Player player) {
        // check if player has a saddle in hand
        ItemStack heldItem = player.getInventory().getItemInMainHand();

        if (heldItem.getType() != Material.SADDLE) {
            Utils.sendActionbarMSG(player, "You must hold a saddle to unclaim it.", ChatColor.RED);
            return true;
        }

        if (heldItem.getItemMeta() != null) {
            ItemMeta meta = heldItem.getItemMeta();
            if (meta.getPersistentDataContainer().has(ownerKey, PersistentDataType.STRING)) {
                String owner = meta.getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING);
                // check if player is saddle owner
                if (owner.equals(player.getName())) {
                    // unclaim saddle
                    meta.getPersistentDataContainer().remove(ownerKey);
                    meta.setDisplayName(ChatColor.RESET + "Saddle"); // changes name to default
                    heldItem.setItemMeta(meta);
                    Utils.sendActionbarMSG(player, "You have unclaimed your saddle.", ChatColor.GREEN);
                    return true;
                } else {
                    Utils.sendActionbarMSG(player, "You aren't the owner of this saddle.", ChatColor.RED);
                    return true;
                }
            }
        }

        Utils.sendActionbarMSG(player, "You don't have a saddle to unclaim.", ChatColor.RED);
        return true;
    }
}