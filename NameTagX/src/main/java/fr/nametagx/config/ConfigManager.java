package fr.nametagx.config;

import fr.nametagx.NameTagX;
import fr.nametagx.model.TagData;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private final NameTagX plugin;
    private FileConfiguration groupsConfig;
    private File groupsFile;
    private final Map<String, TagData> groups;

    public ConfigManager(NameTagX plugin) {
        this.plugin = plugin;
        this.groups = new HashMap<>();
        saveDefaultConfigs();
        loadGroupsConfig();
    }

    private void saveDefaultConfigs() {
        if (!new File(plugin.getDataFolder(), "config.yml").exists()) {
            plugin.saveResource("config.yml", false);
        }

        if (!new File(plugin.getDataFolder(), "groups.yml").exists()) {
            plugin.saveResource("groups.yml", false);
        }
        }

    private void loadGroupsConfig() {
        groupsFile = new File(plugin.getDataFolder(), "groups.yml");
        groupsConfig = YamlConfiguration.loadConfiguration(groupsFile);
        loadGroups();
    }

    private void loadGroups() {
        groups.clear();

        ConfigurationSection groupsSection = groupsConfig.getConfigurationSection("groups");
        if (groupsSection == null) {
            plugin.getLogger().warning("Aucun groupe trouvé dans groups.yml!");
            return;
        }

        for (String groupName : groupsSection.getKeys(false)) {
            ConfigurationSection group = groupsSection.getConfigurationSection(groupName);
            if (group == null) continue;

            String prefix = group.getString("prefix", "");
            String suffix = group.getString("suffix", "");
            int priority = group.getInt("priority", 0);

            ChatColor color;
            try {
                String colorName = group.getString("color", "WHITE");
                color = ChatColor.valueOf(colorName.toUpperCase());
            } catch (Exception e) {
                color = ChatColor.WHITE;
                plugin.getLogger().warning("Couleur invalide pour le groupe " + groupName + ": " + group.getString("color"));
            }

            TagData tagData = new TagData(prefix, suffix, color, priority, groupName);
            groups.put(groupName, tagData);

            plugin.getLogger().info("Groupe chargé: " + groupName + " (priorité: " + priority + ")");
        }
    }

    public void reload() {
        plugin.reloadConfig();
        groupsConfig = YamlConfiguration.loadConfiguration(groupsFile);
        loadGroups();
    }

    public TagData getGroup(String groupName) {
        return groups.get(groupName);
    }

    public Map<String, TagData> getAllGroups() {
        return new HashMap<>(groups);
    }

    public String getGroupPermission(String groupName) {
        ConfigurationSection group = groupsConfig.getConfigurationSection("groups." + groupName);
        if (group == null) return null;
        return group.getString("permission");
    }

    public void save() {
        try {
            groupsConfig.save(groupsFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Erreur lors de la sauvegarde de groups.yml: " + e.getMessage());
        }
    }

    public FileConfiguration getGroupsConfig() {
        return groupsConfig;
    }

    public String getMessage(String key) {
        String prefix = plugin.getConfig().getString("messages.prefix", "&8[&6NameTagX&8]&r ");
        String message = plugin.getConfig().getString("messages." + key, key);
        return colorize(prefix + message);
    }

    public boolean getBoolean(String path, boolean defaultValue) {
        return plugin.getConfig().getBoolean(path, defaultValue);
    }

    public int getInt(String path, int defaultValue) {
        return plugin.getConfig().getInt(path, defaultValue);
    }

    public String getString(String path, String defaultValue) {
        return plugin.getConfig().getString(path, defaultValue);
    }

    private String colorize(String text) {
        return text.replace("&", "§");
    }
}