package fr.nametagx.manager;

import fr.nametagx.NameTagX;
import fr.nametagx.model.TagData;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

public class GroupManager {

    private final NameTagX plugin;

    public GroupManager(NameTagX plugin) {
        this.plugin = plugin;
    }

    public TagData getHighestPriorityGroup(Player player) {
        Map<String, TagData> allGroups = plugin.getConfigManager().getAllGroups();

        Optional<TagData> highestGroup = allGroups.entrySet().stream()
                .filter(entry -> hasGroupPermission(player, entry.getKey()))
                .map(Map.Entry::getValue)
                .max(Comparator.comparingInt(TagData::getPriority));

        return highestGroup.orElse(null);
    }

    public boolean hasGroupPermission(Player player, String groupName) {
        String permission = plugin.getConfigManager().getGroupPermission(groupName);

        if (permission == null || permission.isEmpty()) {
            return true;
        }

        return player.hasPermission(permission);
    }

    public Map<String, TagData> getPlayerGroups(Player player) {
        return plugin.getConfigManager().getAllGroups().entrySet().stream()
                .filter(entry -> hasGroupPermission(player, entry.getKey()))
                .collect(java.util.stream.Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    public boolean groupExists(String groupName) {
        return plugin.getConfigManager().getGroup(groupName) != null;
    }

    public int getGroupCount() {
        return plugin.getConfigManager().getAllGroups().size();
    }
}