package dev.revere.alley.feature.scoreboard;

import dev.revere.alley.Alley;
import dev.revere.alley.api.assemble.interfaces.IAssembleAdapter;
import dev.revere.alley.feature.combat.CombatService;
import dev.revere.alley.feature.kit.settings.impl.KitSettingBattleRushImpl;
import dev.revere.alley.feature.kit.settings.impl.KitSettingBoxingImpl;
import dev.revere.alley.feature.kit.settings.impl.KitSettingLivesImpl;
import dev.revere.alley.feature.kit.settings.impl.KitSettingStickFightImpl;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.game.match.impl.MatchRegularImpl;
import dev.revere.alley.game.match.impl.MatchRoundsRegularImpl;
import dev.revere.alley.game.match.impl.kit.MatchStickFightImpl;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.TimeUtil;
import dev.revere.alley.tool.animation.internal.impl.DotAnimationImpl;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.reflection.BukkitReflection;
import dev.revere.alley.util.visual.ScoreboardUtil;
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
public class ScoreboardVisualizer implements IAssembleAdapter {
    protected final Alley plugin;
    protected final DotAnimationImpl dotAnimation;

    /**
     * Constructor for the ScoreboardVisualizer class.
     *
     * @param plugin The Alley plugin instance.
     */
    public ScoreboardVisualizer(Alley plugin) {
        this.plugin = plugin;
        this.dotAnimation = new DotAnimationImpl();
    }
    
    /**
     * Get the title of the scoreboard.
     *
     * @param player The player to get the title for.
     * @return The title of the scoreboard.
     */
    @Override
    public String getTitle(Player player) {
        return CC.translate(this.plugin.getScoreboardTitleAnimator().getText()
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
        if (this.plugin.getProfileService().getProfile(player.getUniqueId()).getProfileData().getProfileSettingData().isScoreboardEnabled()) {
            Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
            List<String> lines = new ArrayList<>();
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

            if (this.plugin.getConfigService().getConfig("providers/scoreboard.yml").getConfigurationSection("scoreboard.lines") == null) {
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
                    for (String line : this.plugin.getConfigService().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.lobby")) {
                        lines.add(CC.translate(line)
                                .replaceAll("\\{sidebar}", this.getScoreboardLines(player))
                                .replaceAll("\\{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                                .replaceAll("\\{playing}", String.valueOf(this.plugin.getProfileService().getProfiles().values().stream().filter(profile1 -> profile1.getState() == EnumProfileState.PLAYING).count()))
                                .replaceAll("\\{in-queue}", String.valueOf(this.plugin.getProfileService().getProfiles().values().stream().filter(profile1 -> profile1.getState() == EnumProfileState.WAITING).count())));
                    }

                    if (profile.getParty() != null) {
                        for (String line : this.plugin.getConfigService().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.party-addition")) {
                            lines.add(CC.translate(line)
                                    .replaceAll("\\{sidebar}", this.getScoreboardLines(player))
                                    .replaceAll("\\{party-size}", String.valueOf(profile.getParty().getMembers().size()))
                                    .replaceAll("\\{party-leader}", profile.getParty().getLeader().getName()));
                        }
                    }

                    break;
                case WAITING:
                    for (String line : this.plugin.getConfigService().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.waiting")) {
                        lines.add(CC.translate(line)
                                .replaceAll("\\{sidebar}", this.getScoreboardLines(player))
                                .replaceAll("\\{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                                .replaceAll("\\{playing}", String.valueOf(this.plugin.getProfileService().getProfiles().values().stream().filter(profile1 -> profile1.getState() == EnumProfileState.PLAYING).count()))
                                .replaceAll("\\{in-queue}", String.valueOf(this.plugin.getProfileService().getProfiles().values().stream().filter(profile1 -> profile1.getState() == EnumProfileState.WAITING).count()))
                                .replaceAll("\\{queued-type}", this.plugin.getProfileService().getProfile(player.getUniqueId()).getQueueProfile().getQueue().getQueueType())
                                .replaceAll("\\{queued-time}", String.valueOf(this.plugin.getProfileService().getProfile(player.getUniqueId()).getQueueProfile().getFormattedElapsedTime()))
                                .replaceAll("\\{dot-animation}", this.dotAnimation.getCurrentFrame())
                                .replaceAll("\\{queued-kit}", String.valueOf(this.plugin.getProfileService().getProfile(player.getUniqueId()).getQueueProfile().getQueue().getKit().getName())));
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
                        if (profile.getMatch().getKit().isSettingEnabled(KitSettingBattleRushImpl.class) && ((MatchRoundsRegularImpl) profile.getMatch()).getCurrentRound() > 0) {
                            MatchRoundsRegularImpl roundsMatch = (MatchRoundsRegularImpl) profile.getMatch();
                            this.replaceBattleRushLines(player, lines, opponent, roundsMatch, profile);
                        } else if (profile.getMatch().getKit().isSettingEnabled(KitSettingStickFightImpl.class) && ((MatchRoundsRegularImpl) profile.getMatch()).getCurrentRound() > 0) {
                            this.replaceStickFightLines(player, lines, opponent, (MatchStickFightImpl) profile.getMatch(), profile);
                        } else {
                            for (String line : this.plugin.getConfigService().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.starting")) {
                                lines.add(CC.translate(line)
                                        .replaceAll("\\{sidebar}", this.getScoreboardLines(player))
                                        .replaceAll("\\{opponent}", opponent.getPlayer().getUsername())
                                        .replaceAll("\\{opponent-ping}", String.valueOf(BukkitReflection.getPing(opponent.getPlayer().getPlayer())))
                                        .replaceAll("\\{player-ping}", String.valueOf(BukkitReflection.getPing(player)))
                                        .replaceAll("\\{duration}", profile.getMatch().getDuration())
                                        .replaceAll("\\{arena}", profile.getMatch().getArena().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getArena().getDisplayName())
                                        .replaceAll("\\{dot-animation}", this.dotAnimation.getCurrentFrame())
                                        .replaceAll("\\{kit}", profile.getMatch().getKit().getDisplayName()));
                            }
                        }
                    } else if (profile.getMatch().getState() == EnumMatchState.ENDING_MATCH) {
                        for (String line : this.plugin.getConfigService().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.ending")) {
                            lines.add(CC.translate(line)
                                    .replaceAll("\\{sidebar}", this.getScoreboardLines(player))
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
                        for (String line : this.plugin.getConfigService().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.playing.boxing-match")) {
                            lines.add(CC.translate(line)
                                    .replaceAll("\\{sidebar}", this.getScoreboardLines(player))
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
                    } else if (profile.getMatch().getKit().isSettingEnabled(KitSettingBattleRushImpl.class)) {
                        MatchRoundsRegularImpl roundsMatch = (MatchRoundsRegularImpl) profile.getMatch();
                        this.replaceBattleRushLines(player, lines, opponent, roundsMatch, profile);
                    } else if (profile.getMatch().getKit().isSettingEnabled(KitSettingStickFightImpl.class)) {
                        this.replaceStickFightLines(player, lines, opponent, (MatchStickFightImpl) profile.getMatch(), profile);
                    } else if (profile.getMatch().getKit().isSettingEnabled(KitSettingLivesImpl.class)) {
                        for (String line : this.plugin.getConfigService().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.playing.lives-match")) {
                            lines.add(CC.translate(line)
                                    .replaceAll("\\{sidebar}", this.getScoreboardLines(player))
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
                        for (String line : this.plugin.getConfigService().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.playing.regular-match")) {
                            lines.add(CC.translate(line)
                                    .replaceAll("\\{sidebar}", this.getScoreboardLines(player))
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
                    if (profile.getMatch() instanceof MatchRegularImpl) {
                        for (String line : this.plugin.getConfigService().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.spectating")) {
                            lines.add(CC.translate(line)
                                    .replaceAll("\\{sidebar}", this.getScoreboardLines(player))
                                    .replaceAll("\\{playerA}", profile.getMatch().getParticipants().get(0).getPlayer().getUsername())
                                    .replaceAll("\\{playerB}", profile.getMatch().getParticipants().get(1).getPlayer().getUsername())
                                    .replaceAll("\\{pingA}", String.valueOf(BukkitReflection.getPing(profile.getMatch().getParticipants().get(0).getPlayer().getPlayer())))
                                    .replaceAll("\\{pingB}", String.valueOf(BukkitReflection.getPing(profile.getMatch().getParticipants().get(1).getPlayer().getPlayer())))
                                    .replaceAll("\\{colorA}", String.valueOf(((MatchRegularImpl) profile.getMatch()).getTeamAColor()))
                                    .replaceAll("\\{colorB}", String.valueOf(((MatchRegularImpl) profile.getMatch()).getTeamBColor()))
                                    .replaceAll("\\{duration}", profile.getMatch().getDuration())
                                    .replaceAll("\\{arena}", profile.getMatch().getArena().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getArena().getDisplayName())
                                    .replaceAll("\\{kit}", profile.getMatch().getKit().getDisplayName()));
                        }
                    }
                    break;
                case FFA:
                    CombatService combatService = this.plugin.getCombatService();
                    List<String> ffaLines = this.plugin.getConfigService().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.ffa");
                    List<String> combatTagLines = this.plugin.getConfigService().getConfig("providers/scoreboard.yml").getStringList("ffa-combat-tag");

                    for (String line : ffaLines) {
                        if (line.contains("{player-combat}")) {
                            if (combatService.isPlayerInCombat(player.getUniqueId())) {
                                for (String combatLine : combatTagLines) {
                                    lines.add(CC.translate(combatLine
                                            .replaceAll("\\{combat-tag}", combatService.getRemainingTimeFormatted(player))));
                                }
                            }
                        } else {
                            lines.add(CC.translate(line)
                                    .replaceAll("\\{sidebar}", this.getScoreboardLines(player))
                                    .replaceAll("\\{kit}", profile.getFfaMatch().getKit().getDisplayName())
                                    .replaceAll("\\{players}", String.valueOf(profile.getFfaMatch().getPlayers().size()))
                                    .replaceAll("\\{zone}", this.plugin.getFfaSpawnService().getCuboid().isIn(player) ? "Spawn" : "Warzone")
                                    .replaceAll("\\{kills}", String.valueOf(profile.getProfileData().getFfaData().get(profile.getFfaMatch().getKit().getName()).getKills()))
                                    .replaceAll("\\{deaths}", String.valueOf(profile.getProfileData().getFfaData().get(profile.getFfaMatch().getKit().getName()).getDeaths()))
                                    .replaceAll("\\{ping}", String.valueOf(BukkitReflection.getPing(player))));
                        }
                    }
                    break;
            }

            List<String> footer = this.plugin.getConfigService().getConfig("providers/scoreboard.yml").getStringList("scoreboard.footer-addition");
            for (String line : footer) {
                lines.add(CC.translate(line).replaceAll("\\{sidebar}", this.getScoreboardLines(player)));
            }
            return lines;
        }
        return null;
    }

    /**
     * Replace the lines for the Battle Rush match.
     *
     * @param player      The player to replace the lines for.
     * @param lines       The lines to replace.
     * @param opponent    The opponent to replace the lines for.
     * @param roundsMatch The rounds match to replace the lines for.
     * @param profile     The profile to replace the lines for.
     */
    private void replaceBattleRushLines(Player player, List<String> lines, GameParticipant<MatchGamePlayerImpl> opponent, MatchRoundsRegularImpl roundsMatch, Profile profile) {
        long elapsedTime = System.currentTimeMillis() - profile.getMatch().getStartTime();
        long remainingTime = Math.max(900_000 - elapsedTime, 0);
        String formattedTime = TimeUtil.millisToFourDigitSecondsTimer(remainingTime);

        for (String line : this.plugin.getConfigService().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.playing.battlerush-match")) {
            lines.add(CC.translate(line)
                    .replaceAll("\\{sidebar}", this.getScoreboardLines(player))
                    .replaceAll("\\{opponent}", opponent.getPlayer().getUsername())
                    .replaceAll("\\{opponent-ping}", String.valueOf(BukkitReflection.getPing(opponent.getPlayer().getPlayer())))
                    .replaceAll("\\{player-ping}", String.valueOf(BukkitReflection.getPing(player)))
                    .replaceAll("\\{time-left}", formattedTime) // Inject formatted time
                    .replaceAll("\\{goals}", ScoreboardUtil.visualizeGoalsAsCircles(roundsMatch.getParticipantA().getPlayer().getData().getGoals(), 3))
                    .replaceAll("\\{opponent-goals}", ScoreboardUtil.visualizeGoalsAsCircles(roundsMatch.getParticipantB().getPlayer().getData().getGoals(), 3))
                    .replaceAll("\\{kills}", String.valueOf(profile.getMatch().getGamePlayer(player).getData().getKills()))
                    .replaceAll("\\{current-round}", String.valueOf(roundsMatch.getCurrentRound()))
                    .replaceAll("\\{duration}", profile.getMatch().getDuration())
                    .replaceAll("\\{color}", String.valueOf(roundsMatch.getTeamAColor()))
                    .replaceAll("\\{opponent-color}", String.valueOf(roundsMatch.getTeamBColor()))
                    .replaceAll("\\{arena}", profile.getMatch().getArena().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getArena().getDisplayName())
                    .replaceAll("\\{kit}", profile.getMatch().getKit().getDisplayName()));
        }
    }

    /**
     * Replace the lines for the Stick Fight match.
     *
     * @param player        The player to replace the lines for.
     * @param lines         The lines to replace.
     * @param opponent      The opponent to replace the lines for.
     * @param stickFightMatch The stick fight match to replace the lines for.
     * @param profile       The profile to replace the lines for.
     */
    private void replaceStickFightLines(Player player, List<String> lines, GameParticipant<MatchGamePlayerImpl> opponent, MatchStickFightImpl stickFightMatch, Profile profile) {
        for (String line : this.plugin.getConfigService().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.playing.stickfight-match")) {
            lines.add(CC.translate(line)
                    .replaceAll("\\{sidebar}", this.getScoreboardLines(player))
                    .replaceAll("\\{opponent}", opponent.getPlayer().getUsername())
                    .replaceAll("\\{opponent-ping}", String.valueOf(BukkitReflection.getPing(opponent.getPlayer().getPlayer())))
                    .replaceAll("\\{player-ping}", String.valueOf(BukkitReflection.getPing(player)))
                    .replaceAll("\\{goals}", ScoreboardUtil.visualizeGoalsAsCircles(stickFightMatch.getParticipantA().getPlayer().getData().getGoals(), 5))
                    .replaceAll("\\{opponent-goals}", ScoreboardUtil.visualizeGoalsAsCircles(stickFightMatch.getParticipantB().getPlayer().getData().getGoals(), 5))
                    .replaceAll("\\{current-round}", String.valueOf(stickFightMatch.getCurrentRound()))
                    .replaceAll("\\{duration}", profile.getMatch().getDuration())
                    .replaceAll("\\{color}", String.valueOf(stickFightMatch.getTeamAColor()))
                    .replaceAll("\\{opponent-color}", String.valueOf(stickFightMatch.getTeamBColor()))
                    .replaceAll("\\{arena}", profile.getMatch().getArena().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getArena().getDisplayName())
                    .replaceAll("\\{kit}", profile.getMatch().getKit().getDisplayName()));
        }
    }

    /**
     * Get the difference in hits between the player and the opponent.
     *
     * @param player   The player to get the hits from.
     * @param opponent The opponent to get the hits from.
     * @return The difference in hits between the player and the opponent.
     */
    private String getBoxingHitDifference(Player player, GameParticipant<MatchGamePlayerImpl> opponent) {
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        if (profile.getMatch().getKit().isSettingEnabled(KitSettingBoxingImpl.class)) {
            int playerHits = profile.getMatch().getGamePlayer(player).getData().getHits();
            int opponentHits = profile.getMatch().getGamePlayer(opponent.getPlayer().getPlayer()).getData().getHits();

            FileConfiguration config = this.plugin.getConfigService().getScoreboardConfig();
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
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        if (profile.getMatch().getKit().isSettingEnabled(KitSettingBoxingImpl.class)) {
            int playerCombo = profile.getMatch().getGamePlayer(player).getData().getCombo();
            int opponentCombo = profile.getMatch().getGamePlayer(opponent.getPlayer().getPlayer()).getData().getCombo();

            FileConfiguration config = this.plugin.getConfigService().getScoreboardConfig();
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

    /**
     * Show the scoreboard lines.
     *
     * @param player The player to show the scoreboard lines for.
     * @return The scoreboard lines.
     */
    public String getScoreboardLines(Player player) {
        Alley alley = this.plugin;
        if (alley.getProfileService().getProfile(player.getUniqueId()).getProfileData().getProfileSettingData().isShowScoreboardLines()) {
            return alley.getConfigService().getConfig("providers/scoreboard.yml").getString("scoreboard.sidebar-format");
        }

        return "";
    }
}