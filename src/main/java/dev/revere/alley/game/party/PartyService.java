package dev.revere.alley.game.party;

import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.arena.IArenaService;
import dev.revere.alley.base.cooldown.Cooldown;
import dev.revere.alley.base.cooldown.ICooldownRepository;
import dev.revere.alley.base.cooldown.enums.EnumCooldownType;
import dev.revere.alley.base.hotbar.IHotbarService;
import dev.revere.alley.base.hotbar.enums.EnumHotbarType;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.base.queue.QueueProfile;
import dev.revere.alley.base.visibility.IVisibilityService;
import dev.revere.alley.config.IConfigService;
import dev.revere.alley.config.locale.impl.PartyLocale;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.game.match.IMatchService;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.player.participant.TeamGameParticipant;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.tool.reflection.IReflectionRepository;
import dev.revere.alley.tool.reflection.impl.TitleReflectionService;
import dev.revere.alley.util.SoundUtil;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.chat.ClickableUtil;
import dev.revere.alley.util.chat.Symbol;
import lombok.Getter;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @date 16/11/2024 - 22:57
 */
@Getter
@Service(provides = IPartyService.class, priority = 230)
public class PartyService implements IPartyService {
    private final IConfigService configService;
    private final IProfileService profileService;
    private final IHotbarService hotbarService;
    private final IReflectionRepository reflectionRepository;
    private final ICooldownRepository cooldownRepository;
    private final IVisibilityService visibilityService;
    private final IMatchService matchService;
    private final IArenaService arenaService;

    private final List<Party> parties = new ArrayList<>();
    private final List<PartyRequest> partyRequests = new ArrayList<>();
    private String chatFormat;

    public PartyService(IConfigService configService, IProfileService profileService, IHotbarService hotbarService, IReflectionRepository reflectionRepository, ICooldownRepository cooldownRepository, IVisibilityService visibilityService, IMatchService matchService, IArenaService arenaService) {
        this.configService = configService;
        this.profileService = profileService;
        this.hotbarService = hotbarService;
        this.reflectionRepository = reflectionRepository;
        this.cooldownRepository = cooldownRepository;
        this.visibilityService = visibilityService;
        this.matchService = matchService;
        this.arenaService = arenaService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.chatFormat = configService.getMessagesConfig().getString("party.chat-format");
    }

    @Override
    public List<Party> getParties() {
        return Collections.unmodifiableList(this.parties);
    }

    @Override
    public void startMatch(Kit kit, AbstractArena arena, Party party) {
        List<Player> allPartyPlayers = party.getMembers().stream()
                .map(Bukkit::getPlayer)
                .collect(Collectors.toList());

        Collections.shuffle(allPartyPlayers);

        Player leaderA = allPartyPlayers.get(0);
        Player leaderB = allPartyPlayers.get(1);

        MatchGamePlayerImpl gameLeaderA = new MatchGamePlayerImpl(leaderA.getUniqueId(), leaderA.getName());
        MatchGamePlayerImpl gameLeaderB = new MatchGamePlayerImpl(leaderB.getUniqueId(), leaderB.getName());

        GameParticipant<MatchGamePlayerImpl> participantA = new TeamGameParticipant<>(gameLeaderA);
        GameParticipant<MatchGamePlayerImpl> participantB = new TeamGameParticipant<>(gameLeaderB);

        int totalPlayers = allPartyPlayers.size();
        int teamATargetSize = totalPlayers / 2;
        int currentTeamACount = 1;

        for (int i = 2; i < allPartyPlayers.size(); i++) {
            Player currentPlayer = allPartyPlayers.get(i);
            MatchGamePlayerImpl gamePlayer = new MatchGamePlayerImpl(currentPlayer.getUniqueId(), currentPlayer.getName());

            if (currentTeamACount < teamATargetSize) {
                participantA.addPlayer(gamePlayer);
                currentTeamACount++;
            } else {
                participantB.addPlayer(gamePlayer);
            }
        }

        this.matchService.createAndStartMatch(
                kit, this.arenaService.selectArenaWithPotentialTemporaryCopy(arena), participantA, participantB, true, false, false
        );
    }

    @Override
    public void createParty(Player player) {
        Profile profile = this.profileService.getProfile(player.getUniqueId());
        if (profile.getState() != EnumProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou cannot create a party in this state."));
            return;
        }

        Party party = new Party(player);

        this.parties.add(party);

        profile.setParty(party);

        this.hotbarService.applyHotbarItems(player);

        this.reflectionRepository.getReflectionService(TitleReflectionService.class).sendTitle(
                player,
                "&a&l" + Symbol.CROSSED_SWORDS + " Party Created",
                "&7Type /p for help.",
                2, 10, 2
        );

        SoundUtil.playCustomSound(player, Sound.FIREWORK_TWINKLE, 1.0F, 1.0F);

        Arrays.asList(
                "",
                "&6&lParty Created &a" + Symbol.TICK,
                " &7Type /p for help.",
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));
    }

    @Override
    public void disbandParty(Player leader) {
        Profile profile = this.profileService.getProfile(leader.getUniqueId());

        Party party = this.getPartyByLeader(leader);

        if (party == null) {
            leader.sendMessage(CC.translate(PartyLocale.NOT_IN_PARTY.getMessage()));
            return;
        }

        if (profile.getQueueProfile() != null) {
            leader.sendMessage(CC.translate("&cYou cannot disband your party while in a queue."));
            return;
        }

        for (UUID memberId : new ArrayList<>(party.getMembers())) {
            Profile memberProfile = profileService.getProfile(memberId);
            if (memberProfile != null && memberProfile.getQueueProfile() != null && memberProfile.getState().equals(EnumProfileState.WAITING)) {
                this.handlePartyMemberLeave(Bukkit.getPlayer(memberId));
            }
        }
        if (profile.getMatch() != null) {
            party.getMembers().forEach(member -> this.setupProfile(Bukkit.getPlayer(member), false));
        }
        party.notifyParty("&6&lParty &7&l" + Symbol.ARROW_R + " &6" + leader.getName() + " &cdisbanded the party.");
        this.parties.remove(party);

        Cooldown cooldown = this.cooldownRepository.getCooldown(leader.getUniqueId(), EnumCooldownType.PARTY_ANNOUNCE_COOLDOWN);
        if (cooldown != null && cooldown.isActive()) {
            cooldown.resetCooldown();
        }

        if (leader.isOnline()) {
            this.setupProfile(leader, false);
        }

        this.reflectionRepository.getReflectionService(TitleReflectionService.class).sendTitle(
                leader,
                "&c&l✖ Party Disbanded",
                "&7You've removed your party.",
                2, 10, 2
        );

        SoundUtil.playBanHammer(leader);
    }

    @Override
    public void leaveParty(Player player) {
        Party party = this.getPartyByMember(player.getUniqueId());
        if (party == null) {
            if (player.isOnline()) {
                player.sendMessage(CC.translate("&cYou are not in a party."));
            }
            return;
        }

        party.getMembers().remove(player.getUniqueId());
        party.notifyParty("&a" + player.getName() + " has left the party.");
        this.setupProfile(player, false);

        this.handlePartyMemberLeave(player);
    }

    @Override
    public void kickMember(Player leader, Player member) {
        Party party = this.getPartyByLeader(leader);
        if (party == null) return;
        party.getMembers().remove(member.getUniqueId());
        party.notifyParty("&c" + member.getName() + " has been kicked from the party.");
        this.setupProfile(member, false);
    }

    @Override
    public void banMember(Player leader, Player target) {
        Party party = this.getPartyByLeader(leader);
        if (party == null) {
            leader.sendMessage(CC.translate("&cYou are not the leader of a party."));
            return;
        }

        if (!party.getMembers().contains(target.getUniqueId())) {
            leader.sendMessage(CC.translate("&cThat player is not in your party."));
            return;
        }

        party.getBannedPlayers().add(target.getUniqueId());
        party.getMembers().remove(target.getUniqueId());
        this.setupProfile(target, false);

        party.notifyParty(CC.translate("&c" + target.getName() + " has been banned from the party."));
        target.sendMessage(CC.translate("&cYou have been banned from the party."));
    }

    @Override
    public void unbanMember(Player leader, Player target) {
        Party party = this.getPartyByLeader(leader);
        if (party == null) {
            leader.sendMessage(CC.translate("&cYou are not the leader of a party."));
            return;
        }

        if (!party.getBannedPlayers().contains(target.getUniqueId())) {
            leader.sendMessage(CC.translate("&cThat player is not banned from your party."));
            return;
        }

        party.getBannedPlayers().remove(target.getUniqueId());
        party.notifyParty(CC.translate("&6" + target.getName() + " &ahas been unbanned from the party and is now able to join again."));
        target.sendMessage(CC.translate("&aYou have been unbanned from &6" + party.getLeader().getName() + "'s &aparty."));
    }

    @Override
    public void joinParty(Player player, Player leader) {
        Profile profile = this.profileService.getProfile(player.getUniqueId());
        if (profile.getState() != EnumProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou must be in lobby to join a party."));
            return;
        }
        Party party = this.getPartyByLeader(leader);
        if (party == null) {
            player.sendMessage(CC.translate("&cThis party does not exist."));
            return;
        }

        Party yourParty = this.getPartyByLeader(player);
        if (yourParty != null) {
            player.sendMessage(CC.translate("&cYou are already in a party."));
            return;
        }

        if (party.getLeader() == player) {
            player.sendMessage(CC.translate("&cYou cannot join your own party."));
            return;
        }

        if (party.getMembers().contains(player.getUniqueId())) {
            player.sendMessage(CC.translate("&cYou are already in this party."));
            return;
        }

        if (party.getBannedPlayers().contains(player.getUniqueId())) {
            player.sendMessage(CC.translate("&cYou are banned from &6" + leader.getName() + "&c's party."));
            return;
        }

        party.getMembers().add(player.getUniqueId());
        party.notifyParty("&a" + player.getName() + " has joined the party.");

        player.sendMessage(CC.translate(PartyLocale.JOINED_PARTY.getMessage().replace("{player}", leader.getName())));

        this.setupProfile(player, true);
    }

    @Override
    public PartyRequest getRequest(Player player) {
        return this.partyRequests.stream()
                .filter(request -> request.getTarget().equals(player))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void removeRequest(PartyRequest request) {
        this.partyRequests.remove(request);
    }

    @Override
    public String getChatFormat() {
        return chatFormat;
    }

    /**
     * Sets up the profile of a player.
     *
     * @param player The player to set up the profile for.
     * @param join   Whether the player is joining a party.
     */
    private void setupProfile(Player player, boolean join) {
        Profile profile = this.profileService.getProfile(player.getUniqueId());

        profile.setParty(join ? this.getPartyByMember(player.getUniqueId()) : null);

        if (profile.getMatch() != null) {
            return;
        }

        if (join && (profile.getState() == EnumProfileState.LOBBY || profile.getState() == EnumProfileState.WAITING)) {
            hotbarService.applyHotbarItems(player, EnumHotbarType.PARTY);
        } else {
            hotbarService.applyHotbarItems(player, EnumHotbarType.LOBBY);
        }

        this.visibilityService.updateVisibility(player);
    }

    @Override
    public void sendInvite(Party party, Player sender, Player target) {
        if (party == null) return;

        PartyRequest request = new PartyRequest(sender, target);
        this.partyRequests.add(request);

        target.sendMessage("");
        target.sendMessage(CC.translate("&6&lParty Invitation"));
        target.sendMessage(CC.translate("&f&l ● &fFrom: &6" + sender.getName()));
        target.sendMessage(CC.translate("&f&l ● &fPlayers: &6" + party.getMembers().size() + "&f/&630")); //TODO: Implement party size limit with permissions ect...
        target.spigot().sendMessage(this.getClickable(sender));
        target.sendMessage("");
    }

    @Override
    public Party getPartyByLeader(Player player) {
        return this.parties.stream()
                .filter(party -> party.getLeader().equals(player))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Party getPartyByMember(UUID uuid) {
        return this.parties.stream()
                .filter(party -> party.getMembers().contains(uuid))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Party getParty(Player player) {
        Party party = getPartyByLeader(player);
        if (party != null) {
            return party;
        }
        return getPartyByMember(player.getUniqueId());
    }

    @Override
    public void announceParty(Party party) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage("");
            player.sendMessage(CC.translate("&6&l" + party.getLeader().getName() + " &a&lis inviting you to join &6&ltheir &a&lparty!"));
            player.spigot().sendMessage(this.getClickable(party.getLeader()));
            player.sendMessage("");
        });
    }

    /**
     * Handles a player leaving a party, specifically notifying the QueueService if they were queuing.
     * This method should be called whenever a party member disconnects or leaves their party.
     *
     * @param player The player who left the party (or disconnected).
     */
    public void handlePartyMemberLeave(Player player) {
        if (player == null) return;

        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile == null || profile.getQueueProfile() == null || profile.getMatch() != null) {
            return;
        }

        QueueProfile associatedQueueProfile = profile.getQueueProfile();
        Queue queue = associatedQueueProfile.getQueue();

        if (queue.isDuos()) {
            Player leader = Bukkit.getPlayer(associatedQueueProfile.getUuid());
            if (leader != null && leader.isOnline()) {
                Party party = getPartyByLeader(leader);
                if (party == null || party.getMembers().size() < 2) {
                    leader.sendMessage(CC.translate("&eA party member has left/disconnected. You are now queuing solo for duos."));
                } else {
                    leader.sendMessage(CC.translate("&eA party member has left/disconnected. Your party size is now " + party.getMembers().size() + "."));
                    if (party.getMembers().size() < 2 && leader.isOnline()) {
                        leader.sendMessage(CC.translate("&eYou are now queuing solo for duos, awaiting a random teammate."));
                    }
                }
            } else {
                queue.removePlayer(associatedQueueProfile);
            }
        } else {
            queue.removePlayer(associatedQueueProfile);
        }

        profile.setQueueProfile(null);
    }

    /**
     * Gets the clickable text component for accepting a party invitation.
     *
     * @param sender The player who sent the invitation.
     * @return The clickable text component.
     */
    private TextComponent getClickable(Player sender) {
        return ClickableUtil.createComponent(
                " &a(Click to accept)",
                "/party accept " + sender.getName(),
                "&aClick to accept &6" + sender.getName() + "&a's party invitation."
        );
    }
}