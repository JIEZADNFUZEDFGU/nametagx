package fr.nametagx.manager;

import fr.nametagx.NameTagX;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LanguageManager {

    private final NameTagX plugin;
    private FileConfiguration langConfig;
    private String currentLang;
    private final Map<String, FileConfiguration> loadedLanguages;

    public LanguageManager(NameTagX plugin) {
        this.plugin = plugin;
        this.loadedLanguages = new HashMap<>();
        this.currentLang = plugin.getConfig().getString("language", "fr_FR");

        File langFolder = new File(plugin.getDataFolder(), "languages");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }

        saveDefaultLanguageFiles();

        loadLanguage(currentLang);
    }

    private void saveDefaultLanguageFiles() {
        String[] languages = {"fr_FR.yml", "en_US.yml", "es_ES.yml", "de_DE.yml"};

        for (String lang : languages) {
            File langFile = new File(plugin.getDataFolder() + "/languages", lang);
            if (!langFile.exists()) {
                plugin.saveResource("languages/" + lang, false);
            }
        }
    }

    public void loadLanguage(String langCode) {
        File langFile = new File(plugin.getDataFolder() + "/languages", langCode + ".yml");

        if (!langFile.exists()) {
            plugin.getLogger().warning("Fichier de langue introuvable: " + langCode + ".yml - Utilisation de fr_FR");
            langCode = "fr_FR";
            langFile = new File(plugin.getDataFolder() + "/languages", langCode + ".yml");
        }

        try {
            langConfig = YamlConfiguration.loadConfiguration(langFile);

            // Charger les valeurs par défaut depuis les ressources
            InputStream defConfigStream = plugin.getResource("languages/" + langCode + ".yml");
            if (defConfigStream != null) {
                FileConfiguration defConfig = YamlConfiguration.loadConfiguration(
                        new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)
                );
                langConfig.setDefaults(defConfig);
            }

            loadedLanguages.put(langCode, langConfig);
            currentLang = langCode;

            plugin.getLogger().info("Langue chargée: " + langCode);
        } catch (Exception e) {
            plugin.getLogger().severe("Erreur lors du chargement de la langue " + langCode + ": " + e.getMessage());
        }
    }

    public String getMessage(String key) {
        String message = langConfig.getString(key);
        if (message == null) {
            plugin.getLogger().warning("Clé de traduction manquante: " + key);
            return key;
        }
        return colorize(message);
    }

    public String getMessage(String key, String... replacements) {
        String message = getMessage(key);

        for (int i = 0; i < replacements.length; i++) {
            message = message.replace("{" + i + "}", replacements[i]);
        }

        message = message.replace("{player}", replacements.length > 0 ? replacements[0] : "");
        message = message.replace("{group}", replacements.length > 1 ? replacements[1] : "");
        message = message.replace("{prefix}", getPrefix());

        return message;
    }

    public String getPrefix() {
        return colorize(langConfig.getString("prefix", "&8[&6NameTagX&8]&r "));
    }

    public String getMessageWithPrefix(String key, String... replacements) {
        return getPrefix() + getMessage(key, replacements);
    }

    private String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public void reload() {
        loadedLanguages.clear();
        String lang = plugin.getConfig().getString("language", "fr_FR");
        loadLanguage(lang);
    }

    public String getCurrentLanguage() {
        return currentLang;
    }

    public String[] getAvailableLanguages() {
        File langFolder = new File(plugin.getDataFolder(), "languages");
        if (!langFolder.exists()) {
            return new String[]{"fr_FR", "en_US"};
        }

        File[] files = langFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) {
            return new String[]{"fr_FR", "en_US"};
        }

        String[] langs = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            langs[i] = files[i].getName().replace(".yml", "");
        }
        return langs;
    }

    public boolean languageExists(String langCode) {
        File langFile = new File(plugin.getDataFolder() + "/languages", langCode + ".yml");
        return langFile.exists();
    }
}