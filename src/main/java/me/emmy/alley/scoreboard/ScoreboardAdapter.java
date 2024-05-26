package me.emmy.alley.scoreboard;

import me.clip.placeholderapi.PlaceholderAPI;
import me.emmy.alley.Alley;
import me.emmy.alley.match.player.GameParticipant;
import me.emmy.alley.match.player.impl.MatchGamePlayerImpl;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.utils.BukkitReflection;
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
        if (Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getProfileData().getProfileSettingData().isScoreboardEnabled()) {
            Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
            List<String> toReturn = new ArrayList<>();
            switch (profile.getState()) {
                case LOBBY:
                    for (String line : Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.lines.lobby")) {
                        String replacedLine = PlaceholderAPI.setPlaceholders(player, line);
                        replacedLine = replacedLine
                                .replaceAll("\\{sidebar\\}", "&7&m----------------------------")
                                .replaceAll("\\{online\\}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                                .replaceAll("\\{playing\\}", String.valueOf(Alley.getInstance().getProfileRepository().getProfiles().values().stream().filter(profile1 -> profile1.getState() == EnumProfileState.PLAYING).count()))
                                .replaceAll("\\{in-queue\\}", String.valueOf(Alley.getInstance().getProfileRepository().getProfiles().values().stream().filter(profile1 -> profile1.getState() == EnumProfileState.WAITING).count()));
                        toReturn.add(CC.translate(replacedLine));
                    }
                    break;
                case WAITING:
                    for (String line : Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.lines.waiting")) {
                        String replacedLine = PlaceholderAPI.setPlaceholders(player, line);
                        replacedLine = replacedLine
                                .replaceAll("\\{sidebar\\}", "&7&m----------------------------")
                                .replaceAll("\\{online\\}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                                .replaceAll("\\{playing\\}", String.valueOf(Alley.getInstance().getProfileRepository().getProfiles().values().stream().filter(profile1 -> profile1.getState() == EnumProfileState.PLAYING).count()))
                                .replaceAll("\\{in-queue\\}", String.valueOf(Alley.getInstance().getProfileRepository().getProfiles().values().stream().filter(profile1 -> profile1.getState() == EnumProfileState.WAITING).count()))
                                .replaceAll("\\{queued-type\\}", Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getQueueProfile().getQueue().getQueueType())
                                .replaceAll("\\{queued-time\\}", String.valueOf(Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getQueueProfile().getFormattedElapsedTime()))
                                .replaceAll("\\{queued-kit\\}", String.valueOf(Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getQueueProfile().getQueue().getKit().getName()));
                        toReturn.add(CC.translate(replacedLine));
                    }
                    break;
                case PLAYING:
                    GameParticipant<MatchGamePlayerImpl> opponent;
                    GameParticipant<MatchGamePlayerImpl> you;

                    if (profile.getMatch().getParticipants().get(0).getPlayer().getUuid().equals(player.getUniqueId())) {
                        opponent = profile.getMatch().getParticipants().get(1);
                        you = profile.getMatch().getParticipants().get(0);
                    } else {
                        opponent = profile.getMatch().getParticipants().get(0);
                        you = profile.getMatch().getParticipants().get(1);
                    }

                    if (opponent == null || you == null) {
                        return null;
                    }


                    for (String line : Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.lines.playing")) {
                        String replacedLine = PlaceholderAPI.setPlaceholders(player, line);

                        replacedLine = replacedLine
                                .replaceAll("\\{sidebar\\}", "&7&m----------------------------")
                                .replaceAll("\\{opponent\\}", opponent.getPlayer().getUsername())
                                .replaceAll("\\{opponent-ping\\}", String.valueOf(BukkitReflection.getPing(opponent.getPlayer().getPlayer())))
                                .replaceAll("\\{player-ping\\}", String.valueOf(BukkitReflection.getPing(player)))
                                .replaceAll("\\{duration\\}", profile.getMatch().getDuration())
                                .replaceAll("\\{arena\\}", profile.getMatch().getMatchArena().getDisplayName())
                                .replaceAll("\\{kit\\}", profile.getMatch().getMatchKit().getDisplayName());
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
        return null;
    }
}