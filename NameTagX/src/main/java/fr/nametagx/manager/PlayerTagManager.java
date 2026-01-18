package fr.nametagx.manager;

import fr.nametagx.NameTagX;
import fr.nametagx.api.NameTagAPI;
import fr.nametagx.model.TagData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerTagManager implements NameTagAPI {

    private final NameTagX plugin;
    private final Map<String, TagData> customTags;

    public PlayerTagManager(NameTagX plugin) {
        this.plugin = plugin;
        this.customTags = new HashMap<>();
    }

    public void updatePlayerTag(Player player) {
        TagData customTag = customTags.get(player.getName());
        if (customTag != null) {
            plugin.getTeamManager().applyTag(player, customTag);
            return;
        }

        TagData highestTag = null;

        for (Map.Entry<String, TagData> entry : plugin.getConfigManager().getAllGroups().entrySet()) {
            String groupName = entry.getKey();
            TagData tag = entry.getValue();

            String permission = plugin.getConfigManager().getGroupPermission(groupName);

            if (permission == null || permission.isEmpty() || player.hasPermission(permission)) {
                if (highestTag == null || tag.getPriority() > highestTag.getPriority()) {
                    highestTag = tag;
                }
            }
        }

        if (highestTag != null) {
            plugin.getTeamManager().applyTag(player, highestTag);
        } else {
            TagData defaultTag = new TagData("§7", "", ChatColor.GRAY, 0, "default");
            plugin.getTeamManager().applyTag(player, defaultTag);
        }
    }

    @Override
    public void setPlayerTag(Player player, TagData tag) {
        customTags.put(player.getName(), tag);
        plugin.getTeamManager().applyTag(player, tag);
    }

    @Override
    public void resetPlayerTag(Player player) {
        customTags.remove(player.getName());
        updatePlayerTag(player);
    }

    @Override
    public TagData getPlayerTag(Player player) {
        TagData customTag = customTags.get(player.getName());
        if (customTag != null) {
            return customTag;
        }

        TagData highestTag = null;

        for (Map.Entry<String, TagData> entry : plugin.getConfigManager().getAllGroups().entrySet()) {
            String groupName = entry.getKey();
            TagData tag = entry.getValue();

            String permission = plugin.getConfigManager().getGroupPermission(groupName);

            if (permission == null || permission.isEmpty() || player.hasPermission(permission)) {
                if (highestTag == null || tag.getPriority() > highestTag.getPriority()) {
                    highestTag = tag;
                }
            }
        }

        return highestTag;
    }

    public void setPlayerGroup(Player player, String groupName) {
        TagData groupTag = plugin.getConfigManager().getGroup(groupName);
        if (groupTag != null) {
            setPlayerTag(player, groupTag);
        }
    }

    public void cleanupPlayer(Player player) {
        customTags.remove(player.getName());
        plugin.getTeamManager().removeFromCurrentTeam(player);
    }
}