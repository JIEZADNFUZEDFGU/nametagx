package fr.nametagx;

import fr.nametagx.api.NameTagAPI;
import fr.nametagx.command.NameTagCommand;
import fr.nametagx.config.ConfigManager;
import fr.nametagx.listener.PlayerJoinListener;
import fr.nametagx.listener.PlayerQuitListener;
import fr.nametagx.listener.PermissionChangeListener;
import fr.nametagx.manager.GroupManager;
import fr.nametagx.manager.LanguageManager;
import fr.nametagx.manager.PlayerTagManager;
import fr.nametagx.manager.TeamManager;
import org.bukkit.plugin.java.JavaPlugin;

public class NameTagX extends JavaPlugin {

    private static NameTagX instance;
    private ConfigManager configManager;
    private TeamManager teamManager;
    private PlayerTagManager playerTagManager;
    private GroupManager groupManager;
    private LanguageManager languageManager;
    private NameTagAPI api;

    @Override
    public void onEnable() {
        instance = this;

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        saveDefaultConfig();

        this.configManager = new ConfigManager(this);
        this.languageManager = new LanguageManager(this);
        this.teamManager = new TeamManager();
        this.groupManager = new GroupManager(this);
        this.playerTagManager = new PlayerTagManager(this);
        this.api = playerTagManager;

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);

        PermissionChangeListener permListener = new PermissionChangeListener(this);
        getServer().getPluginManager().registerEvents(permListener, this);
        permListener.registerLuckPermsHook(); // Hook LuckPerms si disponible

        getCommand("nametag").setExecutor(new NameTagCommand(this));

        // Application tags aux joueurs en ligne
        getServer().getOnlinePlayers().forEach(playerTagManager::updatePlayerTag);

        getLogger().info(languageManager.getMessage("general.enabled", getDescription().getVersion()));
    }

    @Override
    public void onDisable() {
        // Nettoyage teams
        if (teamManager != null) {
            teamManager.cleanup();
        }
        getLogger().info(languageManager != null ?
                languageManager.getMessage("general.disabled") :
                "NameTagX désactivé!");
    }

    public static NameTagX getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public PlayerTagManager getPlayerTagManager() {
        return playerTagManager;
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public NameTagAPI getAPI() {
        return api;
    }
}