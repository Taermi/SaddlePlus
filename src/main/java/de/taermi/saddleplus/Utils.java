package de.taermi.saddleplus;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class Utils {

    /**
     * Sends an actionbar message to the player.
     *
     * @param player  player who gehts the message
     * @param message message
     * @param color   color of message
     */
    public static void sendActionbarMSG(Player player, String message, ChatColor color) {
        player.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(color + message)
        );
    }
}