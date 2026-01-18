package fr.nametagx.api;

import fr.nametagx.model.TagData;
import org.bukkit.entity.Player;

/**
 * API publique du plugin NameTagX
 * Permet aux autres plugins de modifier les nametags
 *
 * Exemple d'utilisation:
 *
 * NameTagX plugin = (NameTagX) Bukkit.getPluginManager().getPlugin("NameTagX");
 * NameTagAPI api = plugin.getAPI();
 *
 * TagData tag = new TagData("§c[VIP] ", "", NamedTextColor.RED, 50, "vip");
 * api.setPlayerTag(player, tag);
 */
public interface NameTagAPI {

    /**
     * Définit un tag personnalisé pour un joueur
     * Ce tag remplace le tag du groupe
     *
     * @param player Le joueur
     * @param tag Les données du tag
     */
    void setPlayerTag(Player player, TagData tag);

    /**
     * Réinitialise le tag d'un joueur
     * Le joueur récupère le tag de son groupe
     *
     * @param player Le joueur
     */
    void resetPlayerTag(Player player);

    /**
     * Récupère le tag actuel d'un joueur
     *
     * @param player Le joueur
     * @return Les données du tag ou null
     */
    TagData getPlayerTag(Player player);
}