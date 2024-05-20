package me.emmy.alley.scoreboard;

import me.clip.placeholderapi.PlaceholderAPI;
import me.emmy.alley.Alley;
import me.emmy.alley.utils.assemble.AssembleAdapter;
import me.emmy.alley.utils.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardAdapter implements AssembleAdapter {

    @Override
    public String getTitle(Player player) {
        return CC.translate(Alley.getInstance().getScoreboardHandler().getText()
                .replaceAll("%server-name%", Bukkit.getServerName())
        );
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> toReturn = new ArrayList<>();
        for (String line : Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.lines")) {
            String replacedLine = PlaceholderAPI.setPlaceholders(player, line);
            replacedLine = replacedLine.replaceAll("\\{sidebar\\}", "&7&m----------------------------")
                    .replaceAll("\\{online\\}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                    .replaceAll("\\{max-online\\}", String.valueOf(Bukkit.getMaxPlayers()));
            toReturn.add(CC.translate(replacedLine));
        }
        return toReturn;
    }
}