package me.emmy.alley.scoreboard;

import me.clip.placeholderapi.PlaceholderAPI;
import me.emmy.alley.Alley;
import me.emmy.alley.kit.settings.impl.KitSettingBoxingImpl;
import me.emmy.alley.match.enums.EnumMatchState;
import me.emmy.alley.match.player.GameParticipant;
import me.emmy.alley.match.player.impl.MatchGamePlayerImpl;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.utils.assemble.AssembleAdapter;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.reflection.BukkitReflection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * @author Emmy
 * @project Alley
 * @date 27/03/2024 - 14:27
 */
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
            if (profile == null) {
                return Arrays.asList(
                        "&cProfile could not load.",
                        "&cTry relogging.",
                        "",
                        "&cIf this issue persists",
                        "&cplease contact the",
                        "&cplugin developer."
                );
            }

            if (Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getConfigurationSection("scoreboard.lines") == null) {
                return Arrays.asList(
                        "&cNo lines found in the",
                        "&cscoreboard.yml file.",
                        "",
                        "&cIf this issue persists",
                        "please contact the",
                        "&cplugin developer."
                );
            }
            switch (profile.getState()) {
                case LOBBY:
                    for (String line : Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.lines.lobby")) {
                        String replacedLine = PlaceholderAPI.setPlaceholders(player, line);
                        replacedLine = replacedLine
                                .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                .replaceAll("\\{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                                .replaceAll("\\{playing}", String.valueOf(Alley.getInstance().getProfileRepository().getProfiles().values().stream().filter(profile1 -> profile1.getState() == EnumProfileState.PLAYING).count()))
                                .replaceAll("\\{in-queue}", String.valueOf(Alley.getInstance().getProfileRepository().getProfiles().values().stream().filter(profile1 -> profile1.getState() == EnumProfileState.WAITING).count()));
                        toReturn.add(CC.translate(replacedLine));
                    }

                    if (profile.getParty() != null) {
                        for (String line : Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.lines.party-addition")) {
                            String replacedLine = PlaceholderAPI.setPlaceholders(player, line);
                            replacedLine = replacedLine
                                    .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                    .replaceAll("\\{party-size}", String.valueOf(profile.getParty().getMembers().size()))
                                    .replaceAll("\\{party-leader}", profile.getParty().getLeader().getName());
                            toReturn.add(CC.translate(replacedLine));
                        }
                    }

                    break;
                case WAITING:
                    for (String line : Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.lines.waiting")) {
                        String replacedLine = PlaceholderAPI.setPlaceholders(player, line);
                        replacedLine = replacedLine
                                .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                .replaceAll("\\{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                                .replaceAll("\\{playing}", String.valueOf(Alley.getInstance().getProfileRepository().getProfiles().values().stream().filter(profile1 -> profile1.getState() == EnumProfileState.PLAYING).count()))
                                .replaceAll("\\{in-queue}", String.valueOf(Alley.getInstance().getProfileRepository().getProfiles().values().stream().filter(profile1 -> profile1.getState() == EnumProfileState.WAITING).count()))
                                .replaceAll("\\{queued-type}", Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getQueueProfile().getQueue().getQueueType())
                                .replaceAll("\\{queued-time}", String.valueOf(Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getQueueProfile().getFormattedElapsedTime()))
                                .replaceAll("\\{queued-kit}", String.valueOf(Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getQueueProfile().getQueue().getKit().getName()));
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

                    if (profile.getMatch().getMatchState() == EnumMatchState.STARTING) {
                        for (String line : Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.lines.starting")) {
                            String replacedLine = PlaceholderAPI.setPlaceholders(player, line);
                            replacedLine = replacedLine
                                    .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                    .replaceAll("\\{opponent}", opponent.getPlayer().getUsername())
                                    .replaceAll("\\{opponent-ping}", String.valueOf(BukkitReflection.getPing(opponent.getPlayer().getPlayer())))
                                    .replaceAll("\\{player-ping}", String.valueOf(BukkitReflection.getPing(player)))
                                    .replaceAll("\\{duration}", profile.getMatch().getDuration())
                                    .replaceAll("\\{arena}", profile.getMatch().getMatchArena().getDisplayName())
                                    .replaceAll("\\{kit}", profile.getMatch().getMatchKit().getDisplayName());
                            toReturn.add(CC.translate(replacedLine));
                        }
                    } else if (profile.getMatch().getMatchState() == EnumMatchState.ENDING_MATCH) {
                        for (String line : Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.lines.ending")) {
                            String replacedLine = PlaceholderAPI.setPlaceholders(player, line);
                            replacedLine = replacedLine
                                    .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                    .replaceAll("\\{opponent}", opponent.getPlayer().getUsername())
                                    .replaceAll("\\{opponent-ping}", String.valueOf(BukkitReflection.getPing(opponent.getPlayer().getPlayer())))
                                    .replaceAll("\\{player-ping}", String.valueOf(BukkitReflection.getPing(player)))
                                    .replaceAll("\\{duration}", profile.getMatch().getDuration())
                                    .replaceAll("\\{arena}", profile.getMatch().getMatchArena().getDisplayName())
                                    .replaceAll("\\{kit}", profile.getMatch().getMatchKit().getDisplayName())
                                    .replaceAll("\\{winner}", opponent.getPlayer().isDead() ? you.getPlayer().getUsername() : opponent.getPlayer().getUsername())
                                    .replaceAll("\\{end-result}", opponent.getPlayer().isDead() ? "&a&lVICTORY!" : "&c&lDEFEAT!");
                            toReturn.add(CC.translate(replacedLine));
                        }
                    } else if (profile.getMatch().getMatchKit().isSettingEnabled(KitSettingBoxingImpl.class)) {
                        for (String line : Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.lines.playing.boxing-match")) {
                            String replacedLine = PlaceholderAPI.setPlaceholders(player, line);
                            replacedLine = replacedLine
                                    .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                    .replaceAll("\\{opponent}", opponent.getPlayer().getUsername())
                                    .replaceAll("\\{opponent-ping}", String.valueOf(BukkitReflection.getPing(opponent.getPlayer().getPlayer())))
                                    .replaceAll("\\{hits}", "null")
                                    .replaceAll("\\{player-hits}", String.valueOf(profile.getMatch().getGamePlayer(player).getData().getHits()))
                                    .replaceAll("\\{opponent-hits}", String.valueOf(profile.getMatch().getGamePlayer(opponent.getPlayer().getPlayer()).getData().getHits()))
                                    .replaceAll("\\{combo}", profile.getMatch().getGamePlayer(player).getData().getCombo() == 0 ? "No Combo" : profile.getMatch().getGamePlayer(player).getData().getCombo() + " Combo")
                                    .replaceAll("\\{player-ping}", String.valueOf(BukkitReflection.getPing(player)))
                                    .replaceAll("\\{duration}", profile.getMatch().getDuration())
                                    .replaceAll("\\{arena}", profile.getMatch().getMatchArena().getDisplayName())
                                    .replaceAll("\\{kit}", profile.getMatch().getMatchKit().getDisplayName());
                            toReturn.add(CC.translate(replacedLine));
                        }
                    } else {
                        for (String line : Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.lines.playing.regular-match")) {
                            String replacedLine = PlaceholderAPI.setPlaceholders(player, line);
                            replacedLine = replacedLine
                                    .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                    .replaceAll("\\{opponent}", opponent.getPlayer().getUsername())
                                    .replaceAll("\\{opponent-ping}", String.valueOf(BukkitReflection.getPing(opponent.getPlayer().getPlayer())))
                                    .replaceAll("\\{player-ping}", String.valueOf(BukkitReflection.getPing(player)))
                                    .replaceAll("\\{duration}", profile.getMatch().getDuration())
                                    .replaceAll("\\{arena}", profile.getMatch().getMatchArena().getDisplayName())
                                    .replaceAll("\\{kit}", profile.getMatch().getMatchKit().getDisplayName());
                            toReturn.add(CC.translate(replacedLine));
                        }
                    }
                    break;
                case SPECTATING:
                    for (String line : Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.lines.spectating")) {
                        String replacedLine = PlaceholderAPI.setPlaceholders(player, line);
                        replacedLine = replacedLine
                                .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                .replaceAll("\\{player1}", profile.getMatch().getParticipants().get(0).getPlayer().getUsername())
                                .replaceAll("\\{player2}", profile.getMatch().getParticipants().get(1).getPlayer().getUsername())
                                .replaceAll("\\{player1-ping}", String.valueOf(BukkitReflection.getPing(profile.getMatch().getParticipants().get(0).getPlayer().getPlayer())))
                                .replaceAll("\\{player2-ping}", String.valueOf(BukkitReflection.getPing(profile.getMatch().getParticipants().get(1).getPlayer().getPlayer())))
                                .replaceAll("\\{duration}", profile.getMatch().getDuration())
                                .replaceAll("\\{arena}", profile.getMatch().getMatchArena().getDisplayName())
                                .replaceAll("\\{kit}", profile.getMatch().getMatchKit().getDisplayName());
                        toReturn.add(CC.translate(replacedLine));
                    }
                    break;
                case FFA:
                    for (String line : Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.lines.ffa")) {
                        String replacedLine = PlaceholderAPI.setPlaceholders(player, line);
                        replacedLine = replacedLine
                                .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                .replaceAll("\\{kit}", profile.getFfaMatch().getKit().getDisplayName())
                                .replaceAll("\\{players}", String.valueOf(profile.getFfaMatch().getPlayers().size()))
                                .replaceAll("\\{zone}", Alley.getInstance().getFfaSpawnHandler().getCuboid().isIn(player) ? "Spawn" : "Warzone")
                                .replaceAll("\\{kills}", String.valueOf(profile.getProfileData().getFfaData().get(profile.getFfaMatch().getKit().getName()).getKills()))
                                .replaceAll("\\{deaths}", String.valueOf(profile.getProfileData().getFfaData().get(profile.getFfaMatch().getKit().getName()).getDeaths()))
                                .replaceAll("\\{ping}", String.valueOf(BukkitReflection.getPing(player)));
                        toReturn.add(CC.translate(replacedLine));
                    }
                    break;
            }
            List<String> footer = Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.footer-addition");
            for (String line : footer) {
                String replacedLine = PlaceholderAPI.setPlaceholders(player, line);
                replacedLine = replacedLine
                        .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getString("scoreboard.sidebar-format"));
                toReturn.add(CC.translate(replacedLine));
            }
            return toReturn;
        }
        return null;
    }
}