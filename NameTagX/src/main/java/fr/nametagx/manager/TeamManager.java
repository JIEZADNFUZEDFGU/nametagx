package fr.nametagx.manager;

import fr.nametagx.model.TagData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;

public class TeamManager {

    private final Scoreboard scoreboard;
    private final Map<String, String> playerTeams;

    public TeamManager() {
        this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        this.playerTeams = new HashMap<>();
    }

    public void applyTag(Player player, TagData tag) {
        // Retirer de l'ancienne team si existe
        removeFromCurrentTeam(player);

        // Nom de team basé sur la priorité pour un tri automatique
        String teamName = String.format("ntag_%03d_%s", tag.getPriority(), tag.getGroupName());

        Team team = getOrCreateTeam(teamName);

        // Configuration de la team (Spigot 1.21)
        team.setPrefix(tag.getPrefix());
        team.setSuffix(tag.getSuffix());
        team.setColor(tag.getColor());

        // Ajout du joueur
        team.addEntry(player.getName());
        playerTeams.put(player.getName(), teamName);
    }

    public void removeFromCurrentTeam(Player player) {
        String currentTeam = playerTeams.get(player.getName());
        if (currentTeam != null) {
            Team team = scoreboard.getTeam(currentTeam);
            if (team != null) {
                team.removeEntry(player.getName());

                // Supprime la team si vide
                if (team.getEntries().isEmpty()) {
                    team.unregister();
                }
            }
            playerTeams.remove(player.getName());
        }
    }

    private Team getOrCreateTeam(String name) {
        Team team = scoreboard.getTeam(name);
        if (team == null) {
            team = scoreboard.registerNewTeam(name);
        }
        return team;
    }

    public void cleanup() {
        scoreboard.getTeams().stream()
                .filter(team -> team.getName().startsWith("ntag_"))
                .forEach(Team::unregister);
        playerTeams.clear();
    }

    public String getPlayerTeamName(String playerName) {
        return playerTeams.get(playerName);
    }
}