package fr.nametagx.model;

import org.bukkit.ChatColor;

public class TagData {

    private final String prefix;
    private final String suffix;
    private final ChatColor color;
    private final int priority;
    private final String groupName;

    public TagData(String prefix, String suffix, ChatColor color, int priority, String groupName) {
        this.prefix = prefix != null ? prefix : "";
        this.suffix = suffix != null ? suffix : "";
        this.color = color != null ? color : ChatColor.WHITE;
        this.priority = priority;
        this.groupName = groupName != null ? groupName : "default";
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public ChatColor getColor() {
        return color;
    }

    public int getPriority() {
        return priority;
    }

    public String getGroupName() {
        return groupName;
    }

    @Override
    public String toString() {
        return "TagData{" +
                "group=" + groupName +
                ", prefix='" + prefix + '\'' +
                ", suffix='" + suffix + '\'' +
                ", color=" + color +
                ", priority=" + priority +
                '}';
    }
}