package me.emmy.alley.scoreboard;

import me.clip.placeholderapi.PlaceholderAPI;
import me.emmy.alley.Alley;
import me.emmy.alley.kit.settings.impl.KitSettingRankedImpl;
import me.emmy.alley.profile.Profile;
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
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        List<String> toReturn = new ArrayList<>();
        switch (profile.getState()) {
            case LOBBY:
                for (String line : Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.lines.lobby")) {
                    String replacedLine = PlaceholderAPI.setPlaceholders(player, line);
                    replacedLine = replacedLine
                            .replaceAll("\\{sidebar\\}", "&7&m----------------------------")
                            .replaceAll("\\{online\\}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                            .replaceAll("\\{playing\\}", String.valueOf(Alley.getInstance().getMatchRepository().getMatches().size()))
                            .replaceAll("\\{in-queue\\}", "null");
                    toReturn.add(CC.translate(replacedLine));
                }
                break;
            case WAITING:
                for (String line : Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.lines.waiting")) {
                    String replacedLine = PlaceholderAPI.setPlaceholders(player, line);
                    replacedLine = replacedLine
                            .replaceAll("\\{sidebar\\}", "&7&m----------------------------")
                            .replaceAll("\\{online\\}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                            .replaceAll("\\{playing\\}", String.valueOf(Alley.getInstance().getMatchRepository().getMatches().size()))
                            .replaceAll("\\{in-queue\\}", "null")

                            // TODO: {queue-type} always returns ranked if the kit has ranked enabled. So even if you queue in the unranked menu, it will still display ranked because as you can tell, the ranked settings is enabled.... :shrug:

                            .replaceAll("\\{queued-type\\}", Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getQueueProfile().getQueue().isRanked() ? "Ranked" : "Unranked")
                            .replaceAll("\\{queued-time\\}", String.valueOf(Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getQueueProfile().getFormattedElapsedTime()))
                            .replaceAll("\\{queued-kit\\}", String.valueOf(Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getQueueProfile().getQueue().getKit().getName()));
                    toReturn.add(CC.translate(replacedLine));
                }
                break;
            case PLAYING:
                for (String line : Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.lines.playing")) {
                    String replacedLine = PlaceholderAPI.setPlaceholders(player, line);
                    replacedLine = replacedLine
                            .replaceAll("\\{sidebar\\}", "&7&m----------------------------")
                            .replaceAll("\\{opponent\\}", "null")
                            .replaceAll("\\{ping\\}", "null")
                            .replaceAll("\\{duration\\}", "null")
                            .replaceAll("\\{kit\\}", "null");
                    toReturn.add(CC.translate(replacedLine));
                }
                break;
            case SPECTATING:
                for (String line : Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.lines.spectating")) {
                    String replacedLine = PlaceholderAPI.setPlaceholders(player, line);
                    replacedLine = replacedLine
                            .replaceAll("\\{player1\\}", "&7&m----------------------------")
                            .replaceAll("\\{player2\\}", "null")
                            .replaceAll("\\{player1-ping\\}", "null")
                            .replaceAll("\\{player2-ping\\}", "null")
                            .replaceAll("\\{duration\\}", "null")
                            .replaceAll("\\{kit\\}", "null");
                    toReturn.add(CC.translate(replacedLine));
                }
                break;
        }
        return toReturn;
    }
}