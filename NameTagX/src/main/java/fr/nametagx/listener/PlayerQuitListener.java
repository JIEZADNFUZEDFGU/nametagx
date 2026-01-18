package fr.nametagx.listener;

import fr.nametagx.NameTagX;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final NameTagX plugin;

    public PlayerQuitListener(NameTagX plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getPlayerTagManager().cleanupPlayer(event.getPlayer());
    }
}