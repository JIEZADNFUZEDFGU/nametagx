package fr.nametagx.listener;

import fr.nametagx.NameTagX;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;


public class PermissionChangeListener implements Listener {

    private final NameTagX plugin;

    public PermissionChangeListener(NameTagX plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPermissionCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().toLowerCase();

        boolean isPermissionCommand =
                message.startsWith("/lp ") ||           // LuckPerms
                        message.startsWith("/luckperms ") ||
                        message.startsWith("/pex ") ||          // PermissionsEx
                        message.startsWith("/permissions ") ||
                        message.startsWith("/manuadd ") ||      // GroupManager
                        message.startsWith("/mangadd ") ||
                        message.startsWith("/perms ") ||
                        message.startsWith("/group ");

        if (isPermissionCommand) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                for (Player online : plugin.getServer().getOnlinePlayers()) {
                    plugin.getPlayerTagManager().updatePlayerTag(online);
                }
            }, 5L);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            plugin.getPlayerTagManager().updatePlayerTag(player);
        }, 2L);
    }

    public void registerLuckPermsHook() {
        if (plugin.getServer().getPluginManager().getPlugin("LuckPerms") != null) {
            try {
                plugin.getLogger().info("LuckPerms détecté! Hook des événements de permissions activé.");
                registerLuckPermsListener();
            } catch (Exception e) {
                plugin.getLogger().warning("Impossible de s'abonner aux événements LuckPerms: " + e.getMessage());
            }
        }
    }

    private void registerLuckPermsListener() {
        try {
            Class.forName("net.luckperms.api.LuckPerms");

            net.luckperms.api.LuckPerms lp = net.luckperms.api.LuckPermsProvider.get();

            lp.getEventBus().subscribe(plugin, net.luckperms.api.event.user.UserDataRecalculateEvent.class, event -> {
                Player player = plugin.getServer().getPlayer(event.getUser().getUniqueId());
                if (player != null && player.isOnline()) {
                    plugin.getServer().getScheduler().runTask(plugin, () -> {
                        plugin.getPlayerTagManager().updatePlayerTag(player);
                    });
                }
            });

            plugin.getLogger().info("Listener LuckPerms enregistré avec succès!");

        } catch (ClassNotFoundException e) {
            plugin.getLogger().info("LuckPerms API non disponible, utilisation de la détection par commandes.");
        }
    }

    public void refreshAllPlayers() {
        for (Player online : plugin.getServer().getOnlinePlayers()) {
            plugin.getPlayerTagManager().updatePlayerTag(online);
        }
    }

    public void schedulePlayerRefresh(Player player, long delayTicks) {
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (player.isOnline()) {
                plugin.getPlayerTagManager().updatePlayerTag(player);
            }
        }, delayTicks);
    }
}