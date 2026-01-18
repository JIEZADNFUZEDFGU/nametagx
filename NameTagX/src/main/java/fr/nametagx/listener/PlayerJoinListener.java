package fr.nametagx.listener;

import fr.nametagx.NameTagX;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final NameTagX plugin;

    public PlayerJoinListener(NameTagX plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getPlayerTagManager().updatePlayerTag(player);

        for (Player online : plugin.getServer().getOnlinePlayers()) {
            if (!online.equals(player)) {
                plugin.getPlayerTagManager().updatePlayerTag(online);
            }
        }
    }
}