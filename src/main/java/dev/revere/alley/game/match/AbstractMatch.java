package dev.revere.alley.game.match;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.arena.AbstractArena;
import dev.revere.alley.feature.arena.impl.StandAloneArena;
import dev.revere.alley.feature.cosmetic.impl.killeffect.AbstractKillEffect;
import dev.revere.alley.feature.cosmetic.impl.killeffect.KillEffectRepository;
import dev.revere.alley.feature.cosmetic.impl.soundeffect.AbstractSoundEffect;
import dev.revere.alley.feature.cosmetic.impl.soundeffect.SoundEffectRepository;
import dev.revere.alley.feature.cosmetic.interfaces.ICosmeticRepository;
import dev.revere.alley.feature.cosmetic.repository.CosmeticRepository;
import dev.revere.alley.feature.division.Division;
import dev.revere.alley.feature.division.tier.DivisionTier;
import dev.revere.alley.feature.hotbar.HotbarService;
import dev.revere.alley.feature.hotbar.enums.EnumHotbarType;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.setting.impl.KitSettingBattleRushImpl;
import dev.revere.alley.feature.kit.setting.impl.KitSettingLivesImpl;
import dev.revere.alley.feature.kit.setting.impl.KitSettingStickFightImpl;
import dev.revere.alley.feature.layout.record.LayoutRecord;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.game.match.impl.MatchRegularImpl;
import dev.revere.alley.game.match.impl.MatchRoundsImpl;
import dev.revere.alley.game.match.impl.kit.MatchStickFightImpl;
import dev.revere.alley.game.match.player.GamePlayer;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.runnable.MatchRunnable;
import dev.revere.alley.game.match.snapshot.Snapshot;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.data.ProfileData;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.visual.ProgressBarUtil;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
@Setter
public abstract class AbstractMatch {
    private final AbstractArena arena;
    private final Kit kit;
    private final Queue queue;

    private final List<UUID> spectators;
    private List<Snapshot> snapshots;

    private MatchRunnable runnable;
    private EnumMatchState state;

    private Map<BlockState, Location> brokenBlocks;
    private Map<BlockState, Location> placedBlocks;

    private boolean ranked;

    private long startTime;
    private long endTime;

    /**
     * Constructor for the AbstractMatch class.
     *
     * @param queue The queue of the match.
     * @param kit   The kit of the match.
     * @param arena The matchArena of the match.
     */
    public AbstractMatch(Queue queue, Kit kit, AbstractArena arena, boolean ranked) {
        this.queue = queue;
        this.kit = kit;
        this.arena = arena;
        this.ranked = ranked;
        this.snapshots = new ArrayList<>();
        this.placedBlocks = new HashMap<>();
        this.brokenBlocks = new HashMap<>();
        this.spectators = new CopyOnWriteArrayList<>();
        Alley.getInstance().getMatchRepository().getMatches().add(this);
    }

    public abstract void handleRespawn(Player player);

    public abstract List<GameParticipant<MatchGamePlayerImpl>> getParticipants();

    public abstract boolean canStartRound();

    public abstract boolean canEndRound();

    public abstract boolean canEndMatch();

    /**
     * Starts the match by setting the state and updating player profiles and running the match runnable.
     */
    public void startMatch() {
        if (this.arena instanceof StandAloneArena) {
            ((StandAloneArena) this.arena).setActive(true);
        }

        this.sendMessage(CC.translate("&7[&bMatch&7] &b" + this.getParticipants().get(0).getPlayer().getUsername() + " &avs &b" + this.getParticipants().get(1).getPlayer().getUsername()));

        this.state = EnumMatchState.STARTING;
        this.runnable = new MatchRunnable(this);
        this.runnable.runTaskTimer(Alley.getInstance(), 0L, 20L);
        this.getParticipants().forEach(this::initializeParticipant);
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Initializes a game participant and updates the player profiles.
     *
     * @param gameParticipant The game participant to initialize.
     */
    private void initializeParticipant(GameParticipant<MatchGamePlayerImpl> gameParticipant) {
        gameParticipant.getPlayers().forEach(gamePlayer -> {
            Player player = Alley.getInstance().getServer().getPlayer(gamePlayer.getUuid());
            if (player != null) {
                Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());
                profile.setState(EnumProfileState.PLAYING);
                profile.setMatch(this);
                this.setupPlayer(player);
            }
        });
    }

    /**
     * Sets up a player for the match.
     *
     * @param player The player to set up.
     */
    public void setupPlayer(Player player) {
        MatchGamePlayerImpl gamePlayer = getGamePlayer(player);
        if (gamePlayer != null) {
            gamePlayer.setDead(false);
            if (!gamePlayer.isDisconnected()) {
                PlayerUtil.reset(player, true);
                this.giveLoadout(player, this.kit);
            }
        }
    }

    /**
     * Gives a loadout to a player.
     *
     * @param player The player to give the kit to.
     */
    public void giveLoadout(Player player, Kit kit) {
        player.getInventory().setArmorContents(kit.getArmor());

        Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());
        if (profile.getProfileData().getLayoutData().getLayouts().size() > 1) {
            LayoutRecord kitLayout = profile.getProfileData().getLayoutData().getLayouts().get(kit.getName()).get(0);
            player.getInventory().setContents(kitLayout.getItems());
        } else {
            // give books
        }

        player.updateInventory();

        this.kit.applyPotionEffects(player);
    }

    public void resetBlockChanges() {
        this.removePlacedBlocks();
        this.placeBrokenBlocks();
    }

    public void endMatch() {
        this.resetBlockChanges();

        this.getParticipants().forEach(this::finalizeParticipant);
        this.spectators.forEach(uuid -> {
            Player player = Alley.getInstance().getServer().getPlayer(uuid);
            if (player != null) {
                this.removeSpectator(player, false);
            }
        });

        Alley.getInstance().getMatchRepository().getMatches().remove(this);
        this.runnable.cancel();

        if (this.arena instanceof StandAloneArena) {
            ((StandAloneArena) this.arena).setActive(false);
        }
    }

    public void placeBrokenBlocks() {
        this.brokenBlocks.forEach((blockState, location) -> location.getBlock().setType(blockState.getType()));
        this.brokenBlocks.clear();
    }

    public void removePlacedBlocks() {
        this.placedBlocks.forEach((blockState, location) -> location.getBlock().setType(Material.AIR));
        this.placedBlocks.clear();
    }

    /**
     * Finalizes a game participant and updates the player profiles.
     *
     * @param gameParticipant The game participant to finalize.
     */
    private void finalizeParticipant(GameParticipant<MatchGamePlayerImpl> gameParticipant) {
        gameParticipant.getPlayers().forEach(gamePlayer -> {
            if (!gamePlayer.isDisconnected()) {
                Player player = Alley.getInstance().getServer().getPlayer(gamePlayer.getUuid());
                if (player != null) {
                    this.resetPlayerState(player);
                    Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());
                    profile.setState(EnumProfileState.LOBBY);
                    profile.setMatch(null);
                    this.teleportPlayerToSpawn(player);
                }
            }
        });
    }

    /**
     * Teleports a player to the spawn and applies spawn items.
     *
     * @param player The player to teleport.
     */
    private void teleportPlayerToSpawn(Player player) {
        if (player == null) return;
        Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());
        Alley.getInstance().getSpawnService().teleportToSpawn(player);

        HotbarService hotbarService = Alley.getInstance().getHotbarService();
        if (profile.getParty() == null) {
            hotbarService.applyHotbarItems(player, EnumHotbarType.LOBBY);
            return;
        }

        hotbarService.applyHotbarItems(player, EnumHotbarType.PARTY);
    }

    /**
     * Teleports a player to the spawn.
     *
     * @param player The player to teleport.
     */
    private void resetPlayerState(Player player) {
        player.setFireTicks(0);
        player.updateInventory();
        PlayerUtil.reset(player, false);
    }

    /**
     * Creates a snapshot of the match.
     *
     * @param loser  The loser of the match.
     * @param winner The winner of the match.
     */
    public void createSnapshot(UUID loser, UUID winner) {
        Snapshot winnerSnapshot = new Snapshot(Bukkit.getPlayer(winner), true);
        winnerSnapshot.setOpponent(loser);
        this.snapshots.add(winnerSnapshot);

        Snapshot loserSnapshot = new Snapshot(Bukkit.getPlayer(loser), false);
        loserSnapshot.setOpponent(winner);
        this.snapshots.add(loserSnapshot);
    }

    /**
     * Handles the death of a player.
     *
     * @param player The player that died.
     */
    public void handleDeath(Player player) {
        if (!(this.state == EnumMatchState.STARTING || this.state == EnumMatchState.RUNNING)) {
            return;
        }

        MatchGamePlayerImpl gamePlayer = this.getGamePlayer(player);
        if (gamePlayer.isDead()) {
            return;
        }

        this.setParticipantAsDead(player, gamePlayer);
        this.notifySpectators(player.getName() + " has died");
        //notifyParticipants(player.getName() + " has died");

        player.setVelocity(new Vector());

        if (this.canEndRound()) {
            this.state = EnumMatchState.ENDING_ROUND;
            this.handleRoundEnd();

            if (this.canEndMatch()) {
                Player killer = Alley.getInstance().getCombatService().getLastAttacker(player);
                if (killer != null) {
                    this.handleEffects(player, killer);
                }

                this.state = EnumMatchState.ENDING_MATCH;
            }
            this.runnable.setStage(4);
        } else {
            this.handleRespawn(player);
        }
    }

    /**
     * Starts the respawn process for a participant.
     *
     * @param player The player to start the respawn process for.
     */
    public void startRespawnProcess(Player player) {
        new BukkitRunnable() {
            int count = 3;

            @Override
            public void run() {
                if (this.count == 0) {
                    this.cancel();
                    handleRespawn(player);
                    return;
                }

                if (getState() == EnumMatchState.ENDING_MATCH || getState() == EnumMatchState.ENDING_ROUND) {
                    this.cancel();
                    return;
                }

                player.sendMessage(CC.translate("&a" + this.count + "..."));
                this.count--;
            }
        }.runTaskTimer(Alley.getInstance(), 0L, 20L);
    }

    /**
     * Sets a participant as dead.
     *
     * @param player     The player to set as dead.
     * @param gamePlayer The game player to set as dead.
     */
    private void setParticipantAsDead(Player player, MatchGamePlayerImpl gamePlayer) {
        if (this.kit.isSettingEnabled(KitSettingLivesImpl.class)) {
            if (this.getParticipant(player).getPlayer().getData().getLives() <= 0) {
                gamePlayer.setDead(true);
            } else {
                this.getParticipant(player).getPlayers().forEach(participant -> participant.setDead(false));
            }
        } else {
            gamePlayer.setDead(true);
        }
    }

    /**
     * Handles the effects of a player.
     *
     * @param player The player to handle the effects of.
     * @param killer The killer of the player.
     */
    private void handleEffects(Player player, Player killer) {
        Profile profile = Alley.getInstance().getProfileService().getProfile(killer.getUniqueId());
        String selectedKillEffectName = profile.getProfileData().getCosmeticData().getSelectedKillEffect();
        String selectedSoundEffectName = profile.getProfileData().getCosmeticData().getSelectedSoundEffect();

        CosmeticRepository cosmeticRepository = Alley.getInstance().getCosmeticRepository();
        for (ICosmeticRepository<?> repository : cosmeticRepository.getCosmeticRepositories().values()) {
            if (repository instanceof KillEffectRepository) {
                KillEffectRepository killEffectRepository = (KillEffectRepository) repository;
                AbstractKillEffect killEffect = killEffectRepository.getByName(selectedKillEffectName);
                killEffect.spawnEffect(player);
            } else if (repository instanceof SoundEffectRepository) {
                SoundEffectRepository soundEffectRepository = (SoundEffectRepository) repository;
                AbstractSoundEffect soundEffect = soundEffectRepository.getByName(selectedSoundEffectName);
                soundEffect.spawnEffect(killer);
            }
        }
    }

    /**
     * Notifies the participants with a message.
     *
     * @param message The message to notify.
     */
    protected void notifyParticipants(String message) {
        this.getParticipants().forEach(gameParticipant -> gameParticipant.getPlayers().forEach(uuid -> {
            Player player = Alley.getInstance().getServer().getPlayer(uuid.getUuid());
            if (player != null) {
                player.sendMessage(CC.translate(message));
            }
        }));
    }

    /**
     * Notifies the spectators with a message.
     *
     * @param message The message to notify.
     */
    protected void notifySpectators(String message) {
        if (this.spectators == null) return;
        this.spectators.stream()
                .map(uuid -> Alley.getInstance().getServer().getPlayer(uuid))
                .filter(Objects::nonNull)
                .forEach(player -> player.sendMessage(CC.translate(message)));
    }

    protected void notifyAll(String message) {
        this.notifyParticipants(message);
        this.notifySpectators(message);
    }

    /**
     * Handles a player leaving the match.
     *
     * @param player The player that left.
     */
    public void handleLeaving(Player player) {
        if (!(this.state == EnumMatchState.STARTING || state == EnumMatchState.RUNNING)) return;

        MatchGamePlayerImpl gamePlayer = getGamePlayer(player);
        if (gamePlayer != null) {
            if (!gamePlayer.isDead()) {
                if (this.kit.isSettingEnabled(KitSettingLivesImpl.class)) {
                    this.getParticipant(player).getPlayer().getData().setLives(0);
                    this.handleDeath(player);
                } else {
                    this.handleDeath(player);
                }
            }
        }
    }

    /**
     * Handles the disconnect of a player.
     *
     * @param player The player that disconnected.
     */
    public void handleDisconnect(Player player) {
        if (!(this.state == EnumMatchState.STARTING || this.state == EnumMatchState.RUNNING)) return;

        MatchGamePlayerImpl gamePlayer = getGamePlayer(player);
        if (gamePlayer != null) {
            gamePlayer.setDisconnected(true);
            if (!gamePlayer.isDead()) {
                this.handleDeath(player);
            }
        }
    }

    /**
     * Handles the start of a round.
     */
    public void handleRoundStart() {
        this.snapshots.clear();
        if (this.kit.isSettingEnabled(KitSettingBattleRushImpl.class) && ((MatchRoundsImpl) this).getCurrentRound() > 0) {
            return;
        } else if (this.kit.isSettingEnabled(KitSettingStickFightImpl.class) && ((MatchStickFightImpl) this).getCurrentRound() > 0) {
            return;
        }

        this.startTime = System.currentTimeMillis();
    }

    /**
     * Handles the end of a round.
     */
    public void handleRoundEnd() {
        this.endTime = System.currentTimeMillis();
        this.getParticipants().forEach(gameParticipant -> {
            if (gameParticipant.isAllDead()) {
                gameParticipant.getPlayers().forEach(gamePlayer -> {
                    Player player = gamePlayer.getPlayer();
                    if (player != null && !gamePlayer.isDead()) {
                        Snapshot snapshot = new Snapshot(player, true);
                        this.snapshots.add(snapshot);
                    }
                });
            }
        });

        MatchRegularImpl match = (MatchRegularImpl) this;
        if (match.getParticipantA().getPlayers().size() == 1 && match.getParticipantB().getPlayers().size() == 1) {
            this.updateSnapshots(match);
        }
    }

    /**
     * Updates the snapshots of a match.
     *
     * @param match The match to update the snapshots of.
     */
    private void updateSnapshots(MatchRegularImpl match) {
        for (Snapshot snapshot : snapshots) {
            if (snapshot.getUuid().equals(match.getParticipantA().getPlayer().getUuid())) {
                snapshot.setOpponent(match.getParticipantB().getPlayer().getUuid());
            } else {
                snapshot.setOpponent(match.getParticipantA().getPlayer().getUuid());
            }
            Alley.getInstance().getSnapshotRepository().getSnapshots().put(snapshot.getUuid(), snapshot);
        }
    }

    /**
     * Adds a player to the list of spectators.
     *
     * @param player The player to add.
     */
    public void addSpectator(Player player) {
        Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.SPECTATING);
        profile.setMatch(this);
        Alley.getInstance().getHotbarService().applyHotbarItems(player, EnumHotbarType.SPECTATOR);

        if (arena.getCenter() == null) {
            player.sendMessage(CC.translate("&cThe arena is not set up for spectating"));
            return;
        }

        player.teleport(arena.getCenter());
        player.spigot().setCollidesWithEntities(false);
        player.setAllowFlight(true);
        player.setFlying(true);

        spectators.add(player.getUniqueId());
        notifyParticipants(player.getName() + " is now spectating the match");
        notifySpectators(player.getName() + " is now spectating the match");
    }

    /**
     * Removes a player from the list of spectators.
     *
     * @param player The player to remove from spectating.
     */
    public void removeSpectator(Player player, boolean notify) {
        Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.LOBBY);
        profile.setMatch(null);

        player.setAllowFlight(false);
        player.setFlying(false);

        this.resetPlayerState(player);
        this.teleportPlayerToSpawn(player);
        this.spectators.remove(player.getUniqueId());

        if (notify) {
            this.notifyParticipants("&b" + player.getName() + " &ais no longer spectating the match");
            this.notifySpectators("&b" + player.getName() + " &ais no longer spectating the match");
        }
    }

    /**
     * Gets the duration of the match.
     *
     * @return The duration of the match.
     */
    public String getDuration() {
        if (this.state == EnumMatchState.STARTING) {
            return this.getFormattedElapsedTime(this.getElapsedTime());
        } else if (this.state == EnumMatchState.ENDING_MATCH) {
            return this.getFormattedElapsedTime(this.endTime - this.startTime);
        } else {
            return this.getFormattedElapsedTime(this.getElapsedTime());
        }
    }

    /**
     * Gets the elapsed time of the match.
     *
     * @return The elapsed time of the match.
     */
    public long getElapsedTime() {
        return System.currentTimeMillis() - this.startTime;
    }

    /**
     * Gets the formatted elapsed time of the match.
     *
     * @return The formatted elapsed time of the match.
     */
    public String getFormattedElapsedTime(long elapsedMillis) {
        long elapsedSeconds = elapsedMillis / 1000;
        return String.format("%02d:%02d", elapsedSeconds / 60, elapsedSeconds % 60);
    }

    /**
     * Gets the game player of a player.
     *
     * @param player The player to get the game player of.
     * @return The game player of the player.
     */
    public MatchGamePlayerImpl getGamePlayer(Player player) {
        return getParticipants().stream()
                .map(GameParticipant::getPlayers)
                .flatMap(List::stream)
                .filter(gamePlayer -> gamePlayer.getUuid().equals(player.getUniqueId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Sends a message to all participants.
     *
     * @param message The message to send.
     */
    public void sendMessage(String message) {
        getParticipants().forEach(gameParticipant -> gameParticipant.getPlayers().forEach(uuid -> {
            Player player = Alley.getInstance().getServer().getPlayer(uuid.getUuid());
            if (player != null) {
                player.sendMessage(message);
            }
        }));

        getSpectators().forEach(uuid -> {
            Player player = Alley.getInstance().getServer().getPlayer(uuid);
            if (player != null) {
                player.sendMessage(message);
            }
        });
    }

    /**
     * Sends a spigot (clickable) message to all participants including spectators.
     *
     * @param message The message to send.
     */
    public void sendSpigotMessage(BaseComponent message) {
        getParticipants().forEach(gameParticipant -> gameParticipant.getPlayers().forEach(uuid -> {
            Player player = Alley.getInstance().getServer().getPlayer(uuid.getUuid());
            if (player != null) {
                player.spigot().sendMessage(message);
            }
        }));

        getSpectators().forEach(uuid -> {
            Player player = Alley.getInstance().getServer().getPlayer(uuid);
            if (player != null) {
                player.spigot().sendMessage(message);
            }
        });
    }

    /**
     * Sends a combined spigot (clickable) message to all participants including spectators.
     *
     * @param message The message to send.
     */
    public void sendCombinedSpigotMessage(BaseComponent... message) {
        getParticipants().forEach(gameParticipant -> gameParticipant.getPlayers().forEach(uuid -> {
            Player player = Alley.getInstance().getServer().getPlayer(uuid.getUuid());
            if (player != null) {
                player.spigot().sendMessage(message);
            }
        }));

        getSpectators().forEach(uuid -> {
            Player player = Alley.getInstance().getServer().getPlayer(uuid);
            if (player != null) {
                player.spigot().sendMessage(message);
            }
        });
    }

    /**
     * Gets a participant by a player.
     *
     * @param player The player to get the participant of.
     * @return The participant of the player.
     */
    public GameParticipant<MatchGamePlayerImpl> getParticipant(Player player) {
        return getParticipants().stream()
                .filter(gameParticipant -> gameParticipant.containsPlayer(player.getUniqueId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Checks if the attacker is in the same participant team as the supposed victim.
     *
     * @param attacker The attacker.
     * @param victim   The victim.
     * @return If the attacker is in the same participant team as the victim.
     */
    public boolean isInSameTeam(Player attacker, Player victim) {
        GameParticipant<MatchGamePlayerImpl> attackerParticipant = this.getParticipant(attacker);
        GameParticipant<MatchGamePlayerImpl> victimParticipant = this.getParticipant(victim);

        return attackerParticipant.equals(victimParticipant);
    }

    /**
     * Sends the division progress to the winner of the match.
     *
     * @param winner The winner of the match.
     */
    public void sendProgressToWinner(Player winner) {
        Profile winnerProfile = Alley.getInstance().getProfileService().getProfile(winner.getUniqueId());
        ProfileData profileData = winnerProfile.getProfileData();
        int wins = profileData.getUnrankedKitData().get(this.getKit().getName()).getWins();

        Division currentDivision = profileData.getUnrankedKitData().get(this.getKit().getName()).getDivision();
        DivisionTier currentTier = profileData.getUnrankedKitData().get(this.getKit().getName()).getTier();

        List<DivisionTier> tiers = currentDivision.getTiers();
        int tierIndex = tiers.indexOf(currentTier);

        int nextTierWins;
        if (tierIndex < tiers.size() - 1) {
            nextTierWins = tiers.get(tierIndex + 1).getRequiredWins();
        } else if (winnerProfile.getNextDivision(this.getKit().getName()) != null) {
            nextTierWins = winnerProfile.getNextDivision(this.getKit().getName()).getTiers().get(0).getRequiredWins();
        } else {
            nextTierWins = currentTier.getRequiredWins();
        }

        String nextRank = winnerProfile.getNextDivisionAndTier(this.getKit().getName());
        String progressBar = ProgressBarUtil.generate(wins, nextTierWins, 12, "■");
        String progressPercent = nextTierWins > 0 ? Math.round((float) wins / nextTierWins * 100) + "%" : "100%";
        int requiredWinsToUnlock = nextTierWins - wins;
        String winOrWins = requiredWinsToUnlock == 1 ? "win" : "wins";

        Arrays.asList(
                "&b&lProgress",
                " &b&l● &fUnlock &b" + nextRank + " &fwith " + requiredWinsToUnlock + " more " + winOrWins + "!",
                "  &7(" + progressBar + "&7) " + progressPercent,
                " &b&l● &fDaily Streak: &b" + "N/A" + " &f(Best: " + "N/A" + ")",
                " &b&l● &fWin Streak: &b" + "N/A" + " &f(Best: " + "N/A" + ")",
                ""
        ).forEach(line -> winner.sendMessage(CC.translate(line)));
    }

    /**
     * Intentionally made to deny player movement during a match countdown.
     *
     * @param participants the participants
     */
    public void denyPlayerMovement(List<GameParticipant<MatchGamePlayerImpl>> participants) {
        if (participants.size() == 2) {
            GameParticipant<?> participantA = participants.get(0);
            GameParticipant<?> participantB = participants.get(1);

            Location locationA = this.arena.getPos1();
            Location locationB = this.arena.getPos2();

            for (GamePlayer gamePlayer : participantA.getPlayers()) {
                Player participantPlayer = gamePlayer.getPlayer();
                if (participantPlayer != null) {
                    this.teleportBackIfMoved(participantPlayer, locationA);
                }
            }

            for (GamePlayer gamePlayer : participantB.getPlayers()) {
                Player participantPlayer = gamePlayer.getPlayer();
                if (participantPlayer != null) {
                    this.teleportBackIfMoved(participantPlayer, locationB);
                }
            }
        }
    }

    /**
     * Teleports the player back to their designated position if they moved.
     *
     * @param player   The player to check.
     * @param location The designated location.
     */
    private void teleportBackIfMoved(Player player, Location location) {
        Location playerLocation = player.getLocation();

        if (playerLocation.getBlockX() != location.getBlockX() || playerLocation.getBlockZ() != location.getBlockZ()) {
            player.teleport(new Location(location.getWorld(), location.getX(), playerLocation.getY(), location.getZ(), playerLocation.getYaw(), playerLocation.getPitch()));
        }
    }

    /**
     * Adds a block to the placed blocks map with the intention to handle block placement and removal.
     *
     * @param blockState The block state to add.
     * @param location   The location of the block.
     */
    public void addBlockToPlacedBlocksMap(BlockState blockState, Location location) {
        this.placedBlocks.put(blockState, location);
    }

    /**
     * Removes a block from the placed blocks map.
     *
     * @param blockState The block state to remove.
     */
    public void removeBlockFromPlacedBlocksMap(BlockState blockState, Location location) {
        this.placedBlocks.remove(blockState, location);
    }

    /**
     * Adds a block to the changed blocks map with the intention to handle block changes.
     *
     * @param blockState The block state to add.
     * @param location   The location of the block.
     */
    public void addBlockToBrokenBlocksMap(BlockState blockState, Location location) {
        this.brokenBlocks.put(blockState, location);
    }

    /**
     * Removes a block from the broken blocks map.
     *
     * @param blockState The block state to remove.
     */
    public void removeBlockFromBrokenBlocksMap(BlockState blockState, Location location) {
        this.brokenBlocks.remove(blockState, location);
    }
}