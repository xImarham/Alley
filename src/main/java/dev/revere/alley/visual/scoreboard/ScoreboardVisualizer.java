package dev.revere.alley.visual.scoreboard;

import dev.revere.alley.Alley;
import dev.revere.alley.api.assemble.AssembleAdapter;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.kit.settings.impl.KitSettingBoxingImpl;
import dev.revere.alley.kit.settings.impl.KitSettingLivesImpl;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.AnimationUtil;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.reflection.BukkitReflection;
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
        return CC.translate(Alley.getInstance().getScoreboardTitleHandler().getText()
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

            if (Alley.getInstance().getConfigHandler().getConfig("providers/scoreboard.yml").getConfigurationSection("scoreboard.lines") == null) {
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
                    for (String line : Alley.getInstance().getConfigHandler().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.lobby")) {
                        toReturn.add(CC.translate(line)
                                .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfig("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                .replaceAll("\\{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                                .replaceAll("\\{playing}", String.valueOf(Alley.getInstance().getProfileRepository().getProfiles().values().stream().filter(profile1 -> profile1.getState() == EnumProfileState.PLAYING).count()))
                                .replaceAll("\\{in-queue}", String.valueOf(Alley.getInstance().getProfileRepository().getProfiles().values().stream().filter(profile1 -> profile1.getState() == EnumProfileState.WAITING).count())));
                    }

                    if (profile.getParty() != null) {
                        for (String line : Alley.getInstance().getConfigHandler().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.party-addition")) {
                            toReturn.add(CC.translate(line)
                                    .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfig("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                    .replaceAll("\\{party-size}", String.valueOf(profile.getParty().getMembers().size()))
                                    .replaceAll("\\{party-leader}", profile.getParty().getLeader().getName()));
                        }
                    }

                    break;
                case WAITING:
                    for (String line : Alley.getInstance().getConfigHandler().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.waiting")) {
                        toReturn.add(CC.translate(line)
                                .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfig("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
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

                    if (profile.getMatch().getState() == EnumMatchState.STARTING) {
                        for (String line : Alley.getInstance().getConfigHandler().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.starting")) {
                            toReturn.add(CC.translate(line)
                                    .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfig("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                    .replaceAll("\\{opponent}", opponent.getPlayer().getUsername())
                                    .replaceAll("\\{opponent-ping}", String.valueOf(BukkitReflection.getPing(opponent.getPlayer().getPlayer())))
                                    .replaceAll("\\{player-ping}", String.valueOf(BukkitReflection.getPing(player)))
                                    .replaceAll("\\{duration}", profile.getMatch().getDuration())
                                    .replaceAll("\\{arena}", profile.getMatch().getArena().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getArena().getDisplayName())
                                    .replaceAll("\\{dot-animation}", AnimationUtil.getDots())
                                    .replaceAll("\\{kit}", profile.getMatch().getKit().getDisplayName()));
                        }
                    } else if (profile.getMatch().getState() == EnumMatchState.ENDING_MATCH) {
                        for (String line : Alley.getInstance().getConfigHandler().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.ending")) {
                            toReturn.add(CC.translate(line)
                                    .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfig("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                    .replaceAll("\\{opponent}", opponent.getPlayer().getUsername())
                                    .replaceAll("\\{opponent-ping}", String.valueOf(BukkitReflection.getPing(opponent.getPlayer().getPlayer())))
                                    .replaceAll("\\{player-ping}", String.valueOf(BukkitReflection.getPing(player)))
                                    .replaceAll("\\{duration}", profile.getMatch().getDuration())
                                    .replaceAll("\\{arena}", profile.getMatch().getArena().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getArena().getDisplayName())
                                    .replaceAll("\\{kit}", profile.getMatch().getKit().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getKit().getDisplayName())
                                    .replaceAll("\\{winner}", opponent.getPlayer().isDead() ? you.getPlayer().getUsername() : opponent.getPlayer().getUsername())
                                    .replaceAll("\\{end-result}", opponent.getPlayer().isDead() ? "&a&lVICTORY!" : "&c&lDEFEAT!"));
                        }
                    } else if (profile.getMatch().getKit().isSettingEnabled(KitSettingBoxingImpl.class)) {
                        for (String line : Alley.getInstance().getConfigHandler().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.playing.boxing-match")) {
                            toReturn.add(CC.translate(line)
                                    .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfig("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                    .replaceAll("\\{opponent}", opponent.getPlayer().getUsername())
                                    .replaceAll("\\{opponent-ping}", String.valueOf(BukkitReflection.getPing(opponent.getPlayer().getPlayer())))
                                    .replaceAll("\\{difference}", getBoxingHitDifference(player, opponent))
                                    .replaceAll("\\{player-hits}", String.valueOf(profile.getMatch().getGamePlayer(player).getData().getHits()))
                                    .replaceAll("\\{opponent-hits}", String.valueOf(profile.getMatch().getGamePlayer(opponent.getPlayer().getPlayer()).getData().getHits()))
                                    .replaceAll("\\{combo}", getBoxingCombo(player, opponent))
                                    .replaceAll("\\{player-ping}", String.valueOf(BukkitReflection.getPing(player)))
                                    .replaceAll("\\{duration}", profile.getMatch().getDuration())
                                    .replaceAll("\\{arena}", profile.getMatch().getArena().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getArena().getDisplayName())
                                    .replaceAll("\\{kit}", profile.getMatch().getKit().getDisplayName()));
                        }
                    } else if (profile.getMatch().getKit().isSettingEnabled(KitSettingLivesImpl.class)) {
                        for (String line : Alley.getInstance().getConfigHandler().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.playing.lives-match")) {
                            toReturn.add(CC.translate(line)
                                    .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfig("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                    .replaceAll("\\{opponent}", opponent.getPlayer().getUsername())
                                    .replaceAll("\\{opponent-ping}", String.valueOf(BukkitReflection.getPing(opponent.getPlayer().getPlayer())))
                                    .replaceAll("\\{player-ping}", String.valueOf(BukkitReflection.getPing(player)))
                                    .replaceAll("\\{player-lives}", String.valueOf(profile.getMatch().getGamePlayer(player).getData().getLives()))
                                    .replaceAll("\\{opponent-lives}", String.valueOf(profile.getMatch().getGamePlayer(opponent.getPlayer().getPlayer()).getData().getLives()))
                                    .replaceAll("\\{duration}", profile.getMatch().getDuration())
                                    .replaceAll("\\{arena}", profile.getMatch().getArena().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getArena().getDisplayName())
                                    .replaceAll("\\{kit}", profile.getMatch().getKit().getDisplayName()));
                        }
                    } else {
                        for (String line : Alley.getInstance().getConfigHandler().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.playing.regular-match")) {
                            toReturn.add(CC.translate(line)
                                    .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfig("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                    .replaceAll("\\{opponent}", opponent.getPlayer().getUsername())
                                    .replaceAll("\\{opponent-ping}", String.valueOf(BukkitReflection.getPing(opponent.getPlayer().getPlayer())))
                                    .replaceAll("\\{player-ping}", String.valueOf(BukkitReflection.getPing(player)))
                                    .replaceAll("\\{duration}", profile.getMatch().getDuration())
                                    .replaceAll("\\{arena}", profile.getMatch().getArena().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getArena().getDisplayName())
                                    .replaceAll("\\{kit}", profile.getMatch().getKit().getDisplayName()));
                        }
                    }
                    break;
                case SPECTATING:
                    for (String line : Alley.getInstance().getConfigHandler().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.spectating")) {
                        toReturn.add(CC.translate(line)
                                .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfig("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                .replaceAll("\\{playerA}", profile.getMatch().getParticipants().get(0).getPlayer().getUsername())
                                .replaceAll("\\{playerB}", profile.getMatch().getParticipants().get(1).getPlayer().getUsername())
                                .replaceAll("\\{pingA}", String.valueOf(BukkitReflection.getPing(profile.getMatch().getParticipants().get(0).getPlayer().getPlayer())))
                                .replaceAll("\\{pingB}", String.valueOf(BukkitReflection.getPing(profile.getMatch().getParticipants().get(1).getPlayer().getPlayer())))
                                .replaceAll("\\{duration}", profile.getMatch().getDuration())
                                .replaceAll("\\{arena}", profile.getMatch().getArena().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getArena().getDisplayName())
                                .replaceAll("\\{kit}", profile.getMatch().getKit().getDisplayName()));
                    }
                    break;
                case FFA:
                    for (String line : Alley.getInstance().getConfigHandler().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.ffa")) {
                        toReturn.add(CC.translate(line)
                                .replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfig("providers/scoreboard.yml").getString("scoreboard.sidebar-format"))
                                .replaceAll("\\{kit}", profile.getFfaMatch().getKit().getDisplayName())
                                .replaceAll("\\{players}", String.valueOf(profile.getFfaMatch().getPlayers().size()))
                                .replaceAll("\\{zone}", Alley.getInstance().getFfaCuboidService().getCuboid().isIn(player) ? "Spawn" : "Warzone")
                                .replaceAll("\\{kills}", String.valueOf(profile.getProfileData().getFfaData().get(profile.getFfaMatch().getKit().getName()).getKills()))
                                .replaceAll("\\{deaths}", String.valueOf(profile.getProfileData().getFfaData().get(profile.getFfaMatch().getKit().getName()).getDeaths()))
                                .replaceAll("\\{ping}", String.valueOf(BukkitReflection.getPing(player))));
                    }
                    break;
            }

            List<String> footer = Alley.getInstance().getConfigHandler().getConfig("providers/scoreboard.yml").getStringList("scoreboard.footer-addition");
            for (String line : footer) {
                toReturn.add(CC.translate(line).replaceAll("\\{sidebar}", Alley.getInstance().getConfigHandler().getConfig("providers/scoreboard.yml").getString("scoreboard.sidebar-format")));
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
        if (profile.getMatch().getKit().isSettingEnabled(KitSettingBoxingImpl.class)) {
            int playerHits = profile.getMatch().getGamePlayer(player).getData().getHits();
            int opponentHits = profile.getMatch().getGamePlayer(opponent.getPlayer().getPlayer()).getData().getHits();

            FileConfiguration config = Alley.getInstance().getConfigHandler().getScoreboardConfig();
            String positiveDifference = config.getString("boxing-hit-difference.positive-difference", "&a(+{difference})");
            String negativeDifference = config.getString("boxing-hit-difference.negative-difference", "&c(-{difference})");
            String zeroDifference = config.getString("boxing-hit-difference.no-difference", "&a(+0)");

            if (playerHits > opponentHits) {
                return CC.translate(positiveDifference.replace("{difference}", String.valueOf(playerHits - opponentHits)));
            } else if (opponentHits > playerHits) {
                return CC.translate(negativeDifference.replace("{difference}", String.valueOf(opponentHits - playerHits)));
            } else {
                return CC.translate(zeroDifference);
            }
        }
        return "null";
    }

    /**
     * Get the combo that either the player, or the opponent, or no one has
     *
     * @param player   The player to get the its combo from.
     * @param opponent The opponent to get its combo from.
     * @return The combo that either the player, or the opponent, or no one has.
     */
    private String getBoxingCombo(Player player, GameParticipant<MatchGamePlayerImpl> opponent) {
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getMatch().getKit().isSettingEnabled(KitSettingBoxingImpl.class)) {
            int playerCombo = profile.getMatch().getGamePlayer(player).getData().getCombo();
            int opponentCombo = profile.getMatch().getGamePlayer(opponent.getPlayer().getPlayer()).getData().getCombo();

            FileConfiguration config = Alley.getInstance().getConfigHandler().getScoreboardConfig();
            String positiveCombo = config.getString("boxing-combo-display.positive-combo", "&a{combo} Combo");
            String negativeCombo = config.getString("boxing-combo-display.negative-combo", "&c{combo} Combo");
            String zeroCombo = config.getString("boxing-combo-display.no-combo", "&fNo Combo");

            if (playerCombo > 1) {
                return CC.translate(positiveCombo.replace("{combo}", String.valueOf(playerCombo)));
            } else if (opponentCombo > 1) {
                return CC.translate(negativeCombo.replace("{combo}", String.valueOf(opponentCombo)));
            } else {
                return CC.translate(zeroCombo);
            }
        }
        return "null";
    }
}
