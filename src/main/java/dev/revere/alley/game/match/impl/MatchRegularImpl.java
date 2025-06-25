package dev.revere.alley.game.match.impl;

import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingRaidingImpl;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.feature.division.Division;
import dev.revere.alley.feature.division.tier.DivisionTier;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.data.AbstractMatchData;
import dev.revere.alley.game.match.data.impl.MatchDataSoloImpl;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.game.match.player.data.MatchGamePlayerData;
import dev.revere.alley.game.match.player.enums.EnumBaseRaiderRole;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.utility.MatchUtility;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.data.ProfileData;
import dev.revere.alley.tool.elo.EloCalculator;
import dev.revere.alley.tool.elo.result.EloResult;
import dev.revere.alley.tool.elo.result.OldEloResult;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.tool.reflection.impl.TitleReflectionService;
import dev.revere.alley.util.ListenerUtil;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.visual.ProgressBarUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
@Setter
public class MatchRegularImpl extends AbstractMatch {
    private final GameParticipant<MatchGamePlayerImpl> participantA;
    private final GameParticipant<MatchGamePlayerImpl> participantB;

    public final ChatColor teamAColor;
    public final ChatColor teamBColor;

    private GameParticipant<MatchGamePlayerImpl> winner;
    private GameParticipant<MatchGamePlayerImpl> loser;

    /**
     * Constructor for the MatchRegularImpl class.
     *
     * @param queue        The queue of the match.
     * @param kit          The kit of the match.
     * @param arena        The arena of the match.
     * @param ranked       Whether the match is ranked or not.
     * @param participantA The first participant.
     * @param participantB The second participant.
     */
    public MatchRegularImpl(Queue queue, Kit kit, AbstractArena arena, boolean ranked, GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB) {
        super(queue, kit, arena, ranked);
        this.participantA = participantA;
        this.participantB = participantB;
        this.teamAColor = ChatColor.BLUE;
        this.teamBColor = ChatColor.RED;
    }

    @Override
    public void setupPlayer(Player player) {
        super.setupPlayer(player);
        this.applyWoolAndArmorColor(player);

        Location spawnLocation = this.participantA.containsPlayer(player.getUniqueId()) ? getArena().getPos1() : getArena().getPos2();
        player.teleport(spawnLocation);

        if (this.getKit().isSettingEnabled(KitSettingRaidingImpl.class)) {
            this.determineRolesAndGiveKit(player);
        }
    }

    @Override
    public List<GameParticipant<MatchGamePlayerImpl>> getParticipants() {
        return Arrays.asList(this.participantA, this.participantB);
    }

    /**
     * Get the team color of a participant.
     *
     * @param participant The participant to get the team color of.
     * @return The team color of the participant.
     */
    public ChatColor getTeamColor(GameParticipant<MatchGamePlayerImpl> participant) {
        return participant == this.participantA ? this.teamAColor : this.teamBColor;
    }

    /**
     * Applies the wool color to the player based on their team.
     *
     * @param player The player to apply the wool color to.
     */
    public void applyWoolAndArmorColor(Player player) {
        GameParticipant<MatchGamePlayerImpl> participant = this.getParticipant(player);
        if (participant == null) {
            return;
        }

        int woolColor = (participant == this.participantA) ? 11 : 14;

        participant.getPlayers().forEach(gamePlayer -> {
            Player teamPlayer = gamePlayer.getPlayer();
            if (teamPlayer == null || !teamPlayer.isOnline()) {
                return;
            }

            teamPlayer.getInventory().all(Material.WOOL).forEach((key, value) ->
                    teamPlayer.getInventory().setItem(key, new ItemBuilder(Material.WOOL).durability(woolColor).amount(64).build())
            );

            Color color = (woolColor == 11) ? Color.fromRGB(0, 102, 255) : Color.fromRGB(255, 0, 0);

            for (int i = 36; i <= 39; i++) {
                ItemStack item = teamPlayer.getInventory().getItem(i);
                if (item != null && item.getType().toString().contains("LEATHER_")) {
                    LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
                    meta.setColor(color);
                    item.setItemMeta(meta);
                    teamPlayer.getInventory().setItem(i, item);
                }
            }

            teamPlayer.updateInventory();
        });
    }

    @Override
    public void handleRoundEnd() {
        this.winner = this.participantA.isAllDead() ? this.participantB : this.participantA;
        this.loser = this.participantA.isAllDead() ? this.participantA : this.participantB;
        this.loser.getPlayer().setEliminated(true);

        this.winner.getPlayers().forEach(gamePlayer -> {
            this.sendVictory(gamePlayer.getPlayer());
        });

        this.loser.getPlayers().forEach(gamePlayer -> {
            this.sendDefeat(gamePlayer.getPlayer());
        });

        if (this.isTeamMatch()) {
            MatchUtility.sendConjoinedMatchResult(this, this.winner, this.loser);
        } else {
            MatchUtility.sendMatchResult(this, this.winner.getPlayer().getPlayer().getName(), this.loser.getPlayer().getPlayer().getName());
        }

        if (!this.getSpectators().isEmpty()) {
            this.broadcastAndStopSpectating();
        }

        if (this.isAffectStatistics()) {
            this.handleData(this.winner, this.loser, this.participantA, this.participantB);

            ProfileData profileData = this.plugin.getProfileService().getProfile(this.winner.getPlayer().getUuid()).getProfileData();
            Division currentDivision = profileData.getUnrankedKitData().get(this.getKit().getName()).getDivision();
            DivisionTier currentTier = profileData.getUnrankedKitData().get(this.getKit().getName()).getTier();

            if (!this.isRanked()) {
                this.sendProgressToWinner(this.winner.getPlayer().getPlayer(), currentDivision, currentTier);
            }
        }

        super.handleRoundEnd();
    }

    /**
     * Send the winner title to the player.
     *
     * @param player The player to send the title to.
     */
    private void sendVictory(Player player) {
        this.plugin.getReflectionRepository().getReflectionService(TitleReflectionService.class).sendTitle(
                player,
                "&6Victory!",
                "&fYou have won the match!",
                2, 20, 2
        );
    }

    /**
     * Send the loser title to the player.
     *
     * @param player The player to send the title to.
     */
    private void sendDefeat(Player player) {
        this.plugin.getReflectionRepository().getReflectionService(TitleReflectionService.class).sendTitle(
                player,
                "&cDefeat!",
                "&fYou have lost the match!",
                2, 20, 2
        );
    }

    /**
     * Method to handle the data of the match such as elo changes and unranked stats.
     *
     * @param winner       The winner of the match.
     * @param loser        The loser of the match.
     * @param participantA The first participant.
     * @param participantB The second participant.
     */
    public void handleData(GameParticipant<MatchGamePlayerImpl> winner, GameParticipant<MatchGamePlayerImpl> loser, GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB) {
        if (!this.isTeamMatch()) {
            if (this.isRanked()) {
                OldEloResult result = this.getOldEloResult(winner, loser);
                EloResult eloResult = this.getEloResult(result.getOldWinnerElo(), result.getOldLoserElo());

                this.handleWinner(eloResult.getNewWinnerElo(), winner);
                this.handleLoser(eloResult.getNewLoserElo(), loser);

                this.sendEloResult(winner.getPlayer().getPlayer().getName(), loser.getPlayer().getPlayer().getName(), result.getOldWinnerElo(), result.getOldLoserElo(), eloResult.getNewWinnerElo(), eloResult.getNewLoserElo());
            } else {
                ProfileService profileService = this.plugin.getProfileService();

                Profile winnerProfile = profileService.getProfile(winner.getPlayer().getUuid());
                winnerProfile.getProfileData().getUnrankedKitData().get(getKit().getName()).incrementWins();
                winnerProfile.getProfileData().incrementUnrankedWins();
                winnerProfile.getProfileData().determineTitles();
                this.createMatchDataForHistory(winner, loser, winnerProfile);

                Profile loserProfile = profileService.getProfile(loser.getPlayer().getUuid());
                loserProfile.getProfileData().getUnrankedKitData().get(getKit().getName()).incrementLosses();
                loserProfile.getProfileData().incrementUnrankedLosses();
                this.createMatchDataForHistory(loser, winner, loserProfile);
            }
        }
    }

    /**
     * Creates match data for the history of the winner.
     *
     * @param winner       The winner of the match.
     * @param loser        The loser of the match.
     * @param profileToAdd The profile to add the match data to.
     */
    public void createMatchDataForHistory(GameParticipant<MatchGamePlayerImpl> winner, GameParticipant<MatchGamePlayerImpl> loser, Profile profileToAdd) {
        AbstractMatchData matchData = new MatchDataSoloImpl(
                this.getKit().getName(),
                this.getArena().getName(),
                winner.getPlayer().getUuid(),
                loser.getPlayer().getUuid()
        );

        profileToAdd.getProfileData().getPreviousMatches().add(matchData);
    }

    /**
     * Sends the elo result message.
     *
     * @param winnerName   The name of the winner.
     * @param loserName    The name of the loser.
     * @param oldEloWinner The old elo of the winner.
     * @param oldEloLoser  The old elo of the loser.
     * @param newEloWinner The new elo of the winner.
     * @param newEloLoser  The new elo of the loser.
     */
    public void sendEloResult(String winnerName, String loserName, int oldEloWinner, int oldEloLoser, int newEloWinner, int newEloLoser) {
        FileConfiguration config = this.plugin.getConfigService().getMessagesConfig();

        List<String> list = config.getStringList("match.ended.elo-changes.format");
        String winnerIndicatorColor = config.getString("match.ended.elo-changes.winner-indicator-color", "&a");
        String loserIndicatorColor = config.getString("match.ended.elo-changes.loser-indicator-color", "&c");

        list.replaceAll(string -> string
                .replace("{winner}", winnerName)
                .replace("{loser}", loserName)
                .replace("{old-winner-elo}", String.valueOf(oldEloWinner))
                .replace("{old-loser-elo}", String.valueOf(oldEloLoser))
                .replace("{new-winner-elo}", String.valueOf(newEloWinner))
                .replace("{new-loser-elo}", String.valueOf(newEloLoser))
                .replace("{winner-indicator}", newEloWinner > oldEloWinner ? "+" : "-")
                .replace("{loser-indicator}", newEloLoser > oldEloLoser ? "+" : "-")
                .replace("{winner-color}", newEloWinner > oldEloWinner ? winnerIndicatorColor : loserIndicatorColor)
                .replace("{loser-color}", newEloLoser > oldEloLoser ? winnerIndicatorColor : loserIndicatorColor)
                .replace("{math-winner-elo}", String.valueOf(Math.abs(oldEloWinner - newEloWinner)))
                .replace("{math-loser-elo}", String.valueOf(Math.abs(oldEloLoser - newEloLoser)))
        );

        list.forEach(this::notifyParticipants);
    }

    /**
     * Sends the progress of the winner to the player.
     *
     * @param winner          The winner of the match.
     * @param currentDivision The current division of the winner.
     * @param currentTier     The current tier of the winner.
     */
    public void sendProgressToWinner(Player winner, Division currentDivision, DivisionTier currentTier) {
        Profile winnerProfile = this.plugin.getProfileService().getProfile(winner.getUniqueId());
        ProfileData profileData = winnerProfile.getProfileData();
        int wins = profileData.getUnrankedKitData().get(this.getKit().getName()).getWins();

        List<DivisionTier> tiers = currentDivision.getTiers(); //division 2
        int tierIndex = tiers.indexOf(currentTier); // 0

        int nextTierWins;
        if (tierIndex < tiers.size() - 1) { // if there is a next tier
            nextTierWins = tiers.get(tierIndex + 1).getRequiredWins(); // 40
        } else if (winnerProfile.getNextDivision(this.getKit().getName()) != null) { // if division doesn't have a next tier, but there is a next division
            nextTierWins = winnerProfile.getNextDivision(this.getKit().getName()).getTiers().get(0).getRequiredWins();
        } else {
            nextTierWins = currentTier.getRequiredWins();
        }

        String nextRank = winnerProfile.getNextDivisionAndTier(this.getKit().getName());
        String progressBar = ProgressBarUtil.generate(wins, wins == currentTier.getRequiredWins() ? currentTier.getRequiredWins() : nextTierWins, 12, "■");
        String progressPercent = nextTierWins > 0 ? Math.round((float) wins / nextTierWins * 100) + "%" : "100%";
        int requiredWinsToUnlock = nextTierWins - wins; // 40 - 10 (first tier of bronze 1)
        String winOrWins = requiredWinsToUnlock == 1 ? "win" : "wins";
        
        String progressLine;
        if (wins == currentTier.getRequiredWins()) {
            progressLine = " &6&l● &fUNLOCKED &6" + currentDivision.getName() + " " + currentTier.getName() + "&f!";
        } else {
            progressLine = " &6&l● &fUnlock &6" + nextRank + " &fwith " + requiredWinsToUnlock + " more " + winOrWins + "!";
        }

        Arrays.asList(
                "&6&lProgress",
                progressLine,
                "  &7(" + progressBar + "&7) " + progressPercent,
                " &6&l● &fDaily Streak: &6" + "N/A" + " &f(Best: " + "N/A" + ")",
                " &6&l● &fWin Streak: &6" + "N/A" + " &f(Best: " + "N/A" + ")",
                ""
        ).forEach(line -> winner.sendMessage(CC.translate(line)));
    }

    /**
     * Method to get the old elo result.
     *
     * @return The old elo result.
     */
    public @NotNull OldEloResult getOldEloResult(GameParticipant<MatchGamePlayerImpl> winner, GameParticipant<MatchGamePlayerImpl> loser) {
        int oldWinnerElo = winner.getPlayer().getElo();
        int oldLoserElo = loser.getPlayer().getElo();
        return new OldEloResult(oldWinnerElo, oldLoserElo);
    }

    /**
     * Method to get the elo result.
     *
     * @param oldWinnerElo The old elo of the winner.
     * @param oldLoserElo  The old elo of the loser.
     * @return The elo result.
     */
    public @NotNull EloResult getEloResult(int oldWinnerElo, int oldLoserElo) {
        EloCalculator eloCalculator = this.plugin.getEloCalculator();
        int newWinnerElo = eloCalculator.determineNewElo(oldWinnerElo, oldLoserElo, true);
        int newLoserElo = eloCalculator.determineNewElo(oldLoserElo, oldWinnerElo, false);
        return new EloResult(newWinnerElo, newLoserElo);
    }

    /**
     * Method to handle the winner.
     *
     * @param elo    The new elo of the winner.
     * @param winner The winner of the match.
     */
    public void handleWinner(int elo, GameParticipant<MatchGamePlayerImpl> winner) {
        Profile winnerProfile = this.plugin.getProfileService().getProfile(winner.getPlayer().getUuid());
        winnerProfile.getProfileData().getRankedKitData().get(getKit().getName()).setElo(elo);
        winnerProfile.getProfileData().getRankedKitData().get(getKit().getName()).incrementWins();
        winnerProfile.getProfileData().incrementRankedWins();
        winnerProfile.getProfileData().updateElo(winnerProfile);
    }

    /**
     * Method to handle the loser.
     *
     * @param elo   The new elo of the loser.
     * @param loser The loser of the match.
     */
    public void handleLoser(int elo, GameParticipant<MatchGamePlayerImpl> loser) {
        Profile loserProfile = this.plugin.getProfileService().getProfile(loser.getPlayer().getUuid());
        loserProfile.getProfileData().getRankedKitData().get(getKit().getName()).setElo(elo);
        loserProfile.getProfileData().getRankedKitData().get(getKit().getName()).incrementLosses();
        loserProfile.getProfileData().incrementRankedLosses();
        loserProfile.getProfileData().updateElo(loserProfile);
    }

    @Override
    public boolean canStartRound() {
        return false;
    }

    @Override
    public boolean canEndRound() {
        return (this.participantA.isAllDead() || this.participantB.isAllDead())
                || (this.participantA.getAllPlayers().stream().allMatch(MatchGamePlayerImpl::isDisconnected)
                || this.participantB.getAllPlayers().stream().allMatch(MatchGamePlayerImpl::isDisconnected));
    }

    @Override
    public boolean canEndMatch() {
        return true;
    }

    @Override
    public void handleRespawn(Player player) {
        player.spigot().respawn();
        PlayerUtil.reset(player, false);
    }

    @Override
    public void handleDeathItemDrop(Player player, PlayerDeathEvent event) {
        event.getDrops().clear();
    }

    @Override
    public void handleDisconnect(Player player) {
        if (!(this.getState() == EnumMatchState.STARTING || this.getState() == EnumMatchState.RUNNING)) {
            return;
        }

        MatchGamePlayerImpl gamePlayer = this.getFromAllGamePlayers(player);
        if (gamePlayer != null) {
            gamePlayer.setDisconnected(true);
            if (!gamePlayer.isDead()) {
                this.handleDeath(player);
            }
        }
    }

    /**
     * Gives the base raiding kit to the player based on their team.
     *
     * @param player The player to give the kit to.
     */
    public void determineRolesAndGiveKit(Player player) {
        if (this.getParticipantA() == null || this.getParticipantB() == null) {
            return;
        }

        EnumBaseRaiderRole role = getParticipantA().containsPlayer(player.getUniqueId())
                ? EnumBaseRaiderRole.TRAPPER
                : EnumBaseRaiderRole.RAIDER;
        Kit kitToGive = this.plugin.getBaseRaidingService().getRaidingKitByRole(role);

        if (kitToGive == null) {
            Logger.log("&cNo kit found for role: " + role.name() + " in base raiding match.");
            return;
        }

        MatchGamePlayerData data = this.getGamePlayer(player).getData();
        data.setRole(role);

        player.getInventory().setArmorContents(kitToGive.getArmor());
        player.getInventory().setContents(kitToGive.getItems());
        player.updateInventory();
    }
}