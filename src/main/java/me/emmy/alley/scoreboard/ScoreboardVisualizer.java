package me.emmy.alley.scoreboard;

import me.emmy.alley.Alley;
import me.emmy.alley.api.assemble.AssembleAdapter;
import me.emmy.alley.config.ConfigHandler;
import me.emmy.alley.kit.settings.impl.KitSettingBoxingImpl;
import me.emmy.alley.kit.settings.impl.KitSettingLivesImpl;
import me.emmy.alley.match.enums.EnumMatchState;
import me.emmy.alley.match.player.GameParticipant;
import me.emmy.alley.match.player.impl.MatchGamePlayerImpl;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.util.AnimationUtil;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.util.reflection.BukkitReflection;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * @author Emmy
 * @project Alley
 * @date 27/03/2024 - 14:27
 */
public class ScoreboardVisualizer implements AssembleAdapter {

    /**
     * Get the title of the scoreboard.
     *
     * @param player The player to get the title for.
     * @return The title of the scoreboard.
     */
    @Override
    public String getTitle(Player player) {
        return CC.translate(Alley.getInstance().getSbTitleHandler().getText()
                .replaceAll("%server-name%", Bukkit.getServerName())
        );
    }

    /**
     * Get the lines of the scoreboard.
     *
     * @param player The player to get the lines for.
     * @return The lines of the scoreboard.
     */
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
                        toReturn.add(CC.translate(line)
                                .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                .replaceAll("\\{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                                .replaceAll("\\{playing}", String.valueOf(Alley.getInstance().getProfileRepository().getProfiles().values().stream().filter(profile1 -> profile1.getState() == EnumProfileState.PLAYING).count()))
                                .replaceAll("\\{in-queue}", String.valueOf(Alley.getInstance().getProfileRepository().getProfiles().values().stream().filter(profile1 -> profile1.getState() == EnumProfileState.WAITING).count())));
                    }

                    if (profile.getParty() != null) {
                        for (String line : Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.lines.party-addition")) {
                            toReturn.add(CC.translate(line)
                                    .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                    .replaceAll("\\{party-size}", String.valueOf(profile.getParty().getMembers().size()))
                                    .replaceAll("\\{party-leader}", profile.getParty().getLeader().getName()));
                        }
                    }

                    break;
                case WAITING:
                    for (String line : Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.lines.waiting")) {
                        toReturn.add(CC.translate(line)
                                .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                .replaceAll("\\{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                                .replaceAll("\\{playing}", String.valueOf(Alley.getInstance().getProfileRepository().getProfiles().values().stream().filter(profile1 -> profile1.getState() == EnumProfileState.PLAYING).count()))
                                .replaceAll("\\{in-queue}", String.valueOf(Alley.getInstance().getProfileRepository().getProfiles().values().stream().filter(profile1 -> profile1.getState() == EnumProfileState.WAITING).count()))
                                .replaceAll("\\{queued-type}", Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getQueueProfile().getQueue().getQueueType())
                                .replaceAll("\\{queued-time}", String.valueOf(Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getQueueProfile().getFormattedElapsedTime()))
                                .replaceAll("\\{dot-animation}", AnimationUtil.getDots())
                                .replaceAll("\\{queued-kit}", String.valueOf(Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getQueueProfile().getQueue().getKit().getName())));
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
                            toReturn.add(CC.translate(line)
                                    .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                    .replaceAll("\\{opponent}", opponent.getPlayer().getUsername())
                                    .replaceAll("\\{opponent-ping}", String.valueOf(BukkitReflection.getPing(opponent.getPlayer().getPlayer())))
                                    .replaceAll("\\{player-ping}", String.valueOf(BukkitReflection.getPing(player)))
                                    .replaceAll("\\{duration}", profile.getMatch().getDuration())
                                    .replaceAll("\\{arena}", profile.getMatch().getMatchArena().getDisplayName())
                                    .replaceAll("\\{dot-animation}", AnimationUtil.getDots())
                                    .replaceAll("\\{kit}", profile.getMatch().getMatchKit().getDisplayName()));
                        }
                    } else if (profile.getMatch().getMatchState() == EnumMatchState.ENDING_MATCH) {
                        for (String line : Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.lines.ending")) {
                            toReturn.add(CC.translate(line)
                                    .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                    .replaceAll("\\{opponent}", opponent.getPlayer().getUsername())
                                    .replaceAll("\\{opponent-ping}", String.valueOf(BukkitReflection.getPing(opponent.getPlayer().getPlayer())))
                                    .replaceAll("\\{player-ping}", String.valueOf(BukkitReflection.getPing(player)))
                                    .replaceAll("\\{duration}", profile.getMatch().getDuration())
                                    .replaceAll("\\{arena}", profile.getMatch().getMatchArena().getDisplayName())
                                    .replaceAll("\\{kit}", profile.getMatch().getMatchKit().getDisplayName())
                                    .replaceAll("\\{winner}", opponent.getPlayer().isDead() ? you.getPlayer().getUsername() : opponent.getPlayer().getUsername())
                                    .replaceAll("\\{end-result}", opponent.getPlayer().isDead() ? "&a&lVICTORY!" : "&c&lDEFEAT!"));
                        }
                    } else if (profile.getMatch().getMatchKit().isSettingEnabled(KitSettingBoxingImpl.class)) {
                        for (String line : Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.lines.playing.boxing-match")) {
                            toReturn.add(CC.translate(line)
                                    .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                    .replaceAll("\\{opponent}", opponent.getPlayer().getUsername())
                                    .replaceAll("\\{opponent-ping}", String.valueOf(BukkitReflection.getPing(opponent.getPlayer().getPlayer())))
                                    .replaceAll("\\{difference}", getBoxingHitDifference(player, opponent))
                                    .replaceAll("\\{player-hits}", String.valueOf(profile.getMatch().getGamePlayer(player).getData().getHits()))
                                    .replaceAll("\\{opponent-hits}", String.valueOf(profile.getMatch().getGamePlayer(opponent.getPlayer().getPlayer()).getData().getHits()))
                                    .replaceAll("\\{combo}", profile.getMatch().getGamePlayer(player).getData().getCombo() == 0 ? "No Combo" : profile.getMatch().getGamePlayer(player).getData().getCombo() + " Combo")
                                    .replaceAll("\\{player-ping}", String.valueOf(BukkitReflection.getPing(player)))
                                    .replaceAll("\\{duration}", profile.getMatch().getDuration())
                                    .replaceAll("\\{arena}", profile.getMatch().getMatchArena().getDisplayName())
                                    .replaceAll("\\{kit}", profile.getMatch().getMatchKit().getDisplayName()));
                        }
                    } else if (profile.getMatch().getMatchKit().isSettingEnabled(KitSettingLivesImpl.class)) {
                        for (String line : Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.lines.playing.lives-match")) {
                            toReturn.add(CC.translate(line)
                                    .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                    .replaceAll("\\{opponent}", opponent.getPlayer().getUsername())
                                    .replaceAll("\\{opponent-ping}", String.valueOf(BukkitReflection.getPing(opponent.getPlayer().getPlayer())))
                                    .replaceAll("\\{player-ping}", String.valueOf(BukkitReflection.getPing(player)))
                                    .replaceAll("\\{player-lives}", String.valueOf(profile.getMatch().getGamePlayer(player).getData().getLives()))
                                    .replaceAll("\\{opponent-lives}", String.valueOf(profile.getMatch().getGamePlayer(opponent.getPlayer().getPlayer()).getData().getLives()))
                                    .replaceAll("\\{duration}", profile.getMatch().getDuration())
                                    .replaceAll("\\{arena}", profile.getMatch().getMatchArena().getDisplayName())
                                    .replaceAll("\\{kit}", profile.getMatch().getMatchKit().getDisplayName()));
                        }
                    } else {
                        for (String line : Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.lines.playing.regular-match")) {
                            toReturn.add(CC.translate(line)
                                    .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                    .replaceAll("\\{opponent}", opponent.getPlayer().getUsername())
                                    .replaceAll("\\{opponent-ping}", String.valueOf(BukkitReflection.getPing(opponent.getPlayer().getPlayer())))
                                    .replaceAll("\\{player-ping}", String.valueOf(BukkitReflection.getPing(player)))
                                    .replaceAll("\\{duration}", profile.getMatch().getDuration())
                                    .replaceAll("\\{arena}", profile.getMatch().getMatchArena().getDisplayName())
                                    .replaceAll("\\{kit}", profile.getMatch().getMatchKit().getDisplayName()));
                        }
                    }
                    break;
                case SPECTATING:
                    for (String line : Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.lines.spectating")) {
                        toReturn.add(CC.translate(line)
                                .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                .replaceAll("\\{playerA}", profile.getMatch().getParticipants().get(0).getPlayer().getUsername())
                                .replaceAll("\\{playerB}", profile.getMatch().getParticipants().get(1).getPlayer().getUsername())
                                .replaceAll("\\{pingA}", String.valueOf(BukkitReflection.getPing(profile.getMatch().getParticipants().get(0).getPlayer().getPlayer())))
                                .replaceAll("\\{pingB}", String.valueOf(BukkitReflection.getPing(profile.getMatch().getParticipants().get(1).getPlayer().getPlayer())))
                                .replaceAll("\\{duration}", profile.getMatch().getDuration())
                                .replaceAll("\\{arena}", profile.getMatch().getMatchArena().getDisplayName())
                                .replaceAll("\\{kit}", profile.getMatch().getMatchKit().getDisplayName()));
                    }
                    break;
                case FFA:
                    for (String line : Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.lines.ffa")) {
                        toReturn.add(CC.translate(line)
                                .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                .replaceAll("\\{kit}", profile.getFfaMatch().getKit().getDisplayName())
                                .replaceAll("\\{players}", String.valueOf(profile.getFfaMatch().getPlayers().size()))
                                .replaceAll("\\{zone}", Alley.getInstance().getFfaSpawnHandler().getCuboid().isIn(player) ? "Spawn" : "Warzone")
                                .replaceAll("\\{kills}", String.valueOf(profile.getProfileData().getFfaData().get(profile.getFfaMatch().getKit().getName()).getKills()))
                                .replaceAll("\\{deaths}", String.valueOf(profile.getProfileData().getFfaData().get(profile.getFfaMatch().getKit().getName()).getDeaths()))
                                .replaceAll("\\{ping}", String.valueOf(BukkitReflection.getPing(player))));
                    }
                    break;
            }

            List<String> footer = Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getStringList("scoreboard.footer-addition");
            for (String line : footer) {
                toReturn.add(CC.translate(line).replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml").getString("scoreboard.sidebar-format")));
            }
            return toReturn;
        }
        return null;
    }

    /**
     * Get the difference in hits between the player and the opponent.
     *
     * @param player   The player to get the hits from.
     * @param opponent The opponent to get the hits from.
     * @return The difference in hits between the player and the opponent.
     */
    private String getBoxingHitDifference(Player player, GameParticipant<MatchGamePlayerImpl> opponent) {
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getMatch().getMatchKit().isSettingEnabled(KitSettingBoxingImpl.class)) {
            int playerHits = profile.getMatch().getGamePlayer(player).getData().getHits();
            int opponentHits = profile.getMatch().getGamePlayer(opponent.getPlayer().getPlayer()).getData().getHits();
            int difference = playerHits - opponentHits;

            FileConfiguration config = ConfigHandler.getInstance().getScoreboardConfig();
            String positiveDifference = config.getString("boxing-placeholder.positive-difference", "&a(+{difference})");
            String negativeDifference = config.getString("boxing-placeholder.negative-difference", "&c({difference})");
            String zeroDifference = config.getString("boxing-placeholder.no-difference", "&a(+0)");

            if (difference > 0) {
                return CC.translate(positiveDifference.replace("{difference}", String.valueOf(difference)));
            } else if (difference < 0) {
                return CC.translate(negativeDifference.replace("{difference}", String.valueOf(difference)));
            } else {
                return CC.translate(zeroDifference);
            }
        }
        return "null";
    }
}