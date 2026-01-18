package fr.nametagx.command;

import fr.nametagx.NameTagX;
import fr.nametagx.model.TagData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NameTagCommand implements CommandExecutor, TabCompleter {

    private final NameTagX plugin;

    public NameTagCommand(NameTagX plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        // /nametag
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String subCmd = args[0].toLowerCase();

        switch (subCmd) {
            case "reload":
                return handleReload(sender);

            case "set":
                return handleSet(sender, args);

            case "reset":
                return handleReset(sender, args);

            case "list":
                return handleList(sender);

            case "info":
                return handleInfo(sender, args);

            case "language":
            case "lang":
                return handleLanguage(sender, args);

            default:
                sendHelp(sender);
                return true;
        }
    }

    private boolean handleReload(CommandSender sender) {
        if (!sender.hasPermission("nametag.reload")) {
            sender.sendMessage(plugin.getLanguageManager().getMessageWithPrefix("general.no-permission"));
            return true;
        }

        plugin.getConfigManager().reload();
        plugin.getLanguageManager().reload();

        for (Player player : Bukkit.getOnlinePlayers()) {
            plugin.getPlayerTagManager().updatePlayerTag(player);
        }

        sender.sendMessage(plugin.getLanguageManager().getMessageWithPrefix("general.reload-success"));
        return true;
    }

    private boolean handleSet(CommandSender sender, String[] args) {
        if (!sender.hasPermission("nametag.admin")) {
            sender.sendMessage(plugin.getLanguageManager().getMessageWithPrefix("general.no-permission"));
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(plugin.getLanguageManager().getMessageWithPrefix("commands.usage-set"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(plugin.getLanguageManager().getMessageWithPrefix("player.not-found", args[1]));
            return true;
        }

        String groupName = args[2];
        TagData group = plugin.getConfigManager().getGroup(groupName);

        if (group == null) {
            sender.sendMessage(plugin.getLanguageManager().getMessageWithPrefix("group.not-found", groupName));
            return true;
        }

        plugin.getPlayerTagManager().setPlayerGroup(target, groupName);
        sender.sendMessage(plugin.getLanguageManager().getMessageWithPrefix("group.set-success", target.getName(), groupName));

        return true;
    }

    private boolean handleReset(CommandSender sender, String[] args) {
        if (!sender.hasPermission("nametag.admin")) {
            sender.sendMessage(plugin.getLanguageManager().getMessageWithPrefix("general.no-permission"));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(plugin.getLanguageManager().getMessageWithPrefix("commands.usage-reset"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(plugin.getLanguageManager().getMessageWithPrefix("player.not-found", args[1]));
            return true;
        }

        plugin.getPlayerTagManager().resetPlayerTag(target);
        sender.sendMessage(plugin.getLanguageManager().getMessageWithPrefix("group.reset-success", target.getName()));

        return true;
    }

    private boolean handleList(CommandSender sender) {
        sender.sendMessage(plugin.getLanguageManager().getMessage("group.list-header"));

        plugin.getConfigManager().getAllGroups().values().stream()
                .sorted((a, b) -> Integer.compare(b.getPriority(), a.getPriority()))
                .forEach(tag -> {
                    sender.sendMessage(plugin.getLanguageManager().getMessage("group.list-format",
                            tag.getGroupName(), String.valueOf(tag.getPriority())));
                });

        return true;
    }

    private boolean handleInfo(CommandSender sender, String[] args) {
        Player target;

        if (args.length < 2) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getLanguageManager().getMessageWithPrefix("commands.usage-info"));
                return true;
            }
            target = (Player) sender;
        } else {
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(plugin.getLanguageManager().getMessageWithPrefix("player.not-found", args[1]));
                return true;
            }
        }

        TagData tag = plugin.getPlayerTagManager().getPlayerTag(target);
        if (tag == null) {
            sender.sendMessage(plugin.getLanguageManager().getMessageWithPrefix("info.no-tag"));
            return true;
        }

        sender.sendMessage(plugin.getLanguageManager().getMessage("info.header", target.getName()));
        sender.sendMessage(plugin.getLanguageManager().getMessage("info.group", tag.getGroupName()));
        sender.sendMessage(plugin.getLanguageManager().getMessage("info.prefix", tag.getPrefix()));
        sender.sendMessage(plugin.getLanguageManager().getMessage("info.suffix", tag.getSuffix()));
        sender.sendMessage(plugin.getLanguageManager().getMessage("info.color", tag.getColor().toString()));
        sender.sendMessage(plugin.getLanguageManager().getMessage("info.priority", String.valueOf(tag.getPriority())));

        return true;
    }

    private boolean handleLanguage(CommandSender sender, String[] args) {
        if (!sender.hasPermission("nametag.admin")) {
            sender.sendMessage(plugin.getLanguageManager().getMessageWithPrefix("general.no-permission"));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(plugin.getLanguageManager().getMessage("language.list-header"));
            sender.sendMessage(plugin.getLanguageManager().getMessage("language.current",
                    plugin.getLanguageManager().getCurrentLanguage()));

            String[] available = plugin.getLanguageManager().getAvailableLanguages();
            sender.sendMessage(plugin.getLanguageManager().getMessage("language.available",
                    String.join(", ", available)));

            return true;
        }

        String newLang = args[1];

        if (!plugin.getLanguageManager().languageExists(newLang)) {
            sender.sendMessage(plugin.getLanguageManager().getMessageWithPrefix("language.not-found", newLang));
            return true;
        }

        plugin.getConfig().set("language", newLang);
        plugin.saveConfig();


        plugin.getLanguageManager().loadLanguage(newLang);

        sender.sendMessage(plugin.getLanguageManager().getMessageWithPrefix("language.changed", newLang));

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(plugin.getLanguageManager().getMessage("help.header"));
        sender.sendMessage(plugin.getLanguageManager().getMessage("help.reload"));
        sender.sendMessage(plugin.getLanguageManager().getMessage("help.set"));
        sender.sendMessage(plugin.getLanguageManager().getMessage("help.reset"));
        sender.sendMessage(plugin.getLanguageManager().getMessage("help.list"));
        sender.sendMessage(plugin.getLanguageManager().getMessage("help.info"));
        sender.sendMessage(plugin.getLanguageManager().getMessage("help.language"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(Arrays.asList("reload", "set", "reset", "list", "info", "language"));
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("info")) {
                return Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .collect(Collectors.toList());
            } else if (args[0].equalsIgnoreCase("language") || args[0].equalsIgnoreCase("lang")) {
                return Arrays.asList(plugin.getLanguageManager().getAvailableLanguages());
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
            return new ArrayList<>(plugin.getConfigManager().getAllGroups().keySet());
        }

        return completions.stream()
                .filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }
}