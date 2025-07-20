package dev.revere.alley.feature.music;

import dev.revere.alley.Alley;
import dev.revere.alley.base.spawn.ISpawnService;
import dev.revere.alley.feature.music.enums.EnumMusicDisc;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.util.TimeUtil;
import dev.revere.alley.util.chat.CC;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldEvent;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author Emmy & Remi
 * @project alley-practice
 * @since 19/07/2025
 */
@Service(provides = IMusicService.class, priority = 175)
public class MusicService implements IMusicService {
    private static final int RECORD_PLAY_EFFECT_ID = 1005;

    private final IProfileService profileService;
    private final ISpawnService spawnService;
    private final Map<UUID, MusicSession> activeSessions = new ConcurrentHashMap<>();
    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    private EnumMusicDisc[] allDiscs;

    /**
     * DI Constructor for the MusicService class.
     *
     * @param profileService The profile service to be used by this music service.
     * @param spawnService   The spawn service to be used by this music service.
     */
    public MusicService(IProfileService profileService, ISpawnService spawnService) {
        this.profileService = profileService;
        this.spawnService = spawnService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.allDiscs = EnumMusicDisc.values();
    }

    @Override
    public void startMusic(Player player) {
        stopMusic(player);

        Profile profile = this.profileService.getProfile(player.getUniqueId());
        if (profile == null || !profile.getProfileData().getSettingData().isLobbyMusicEnabled()) {
            return;
        }

        EnumMusicDisc disc = getRandomSelectedMusicDisc(profile);
        Location jukeboxLocation = spawnService.getLocation();
        sendPlaySoundPacket(player, disc, jukeboxLocation);

        String formattedDuration = TimeUtil.formatTimeFromSeconds(disc.getDuration());
        String message = CC.translate("&7[&6♬&7] &fNow playing: &6" + disc.getTitle() + " &7(" + formattedDuration + ")");
        player.sendMessage(message);

        MusicSession session = new MusicSession(disc, jukeboxLocation);
        MusicTask task = new MusicTask(player, this, profileService);
        session.setTask(task.runTaskTimer(Alley.getInstance(), 20L, 20L));

        activeSessions.put(player.getUniqueId(), session);
    }

    @Override
    public void stopMusic(Player player) {
        MusicSession session = activeSessions.remove(player.getUniqueId());
        if (session != null) {
            sendStopSoundPacket(player, session.getJukeboxLocation());
            session.getTask().cancel();
        }
    }

    @Override
    public EnumMusicDisc getRandomMusicDisc() {
        if (allDiscs == null || allDiscs.length == 0) {
            return null;
        }
        return allDiscs[random.nextInt(allDiscs.length)];
    }

    @Override
    public Set<EnumMusicDisc> getSelectedMusicDiscs(Profile profile) {
        return profile.getProfileData().getMusicData().getSelectedDiscs().stream()
                .map(name -> {
                    try {
                        return EnumMusicDisc.valueOf(name);
                    } catch (IllegalArgumentException e) {
                        Logger.logException("Invalid music disc: " + name + " for " + profile.getUuid(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public EnumMusicDisc getRandomSelectedMusicDisc(Profile profile) {
        Set<EnumMusicDisc> selectedDiscsSet = this.getSelectedMusicDiscs(profile);
        if (selectedDiscsSet.isEmpty()) {
            return this.getRandomMusicDisc();
        }

        List<EnumMusicDisc> selectedDiscs = new ArrayList<>(selectedDiscsSet);
        return selectedDiscs.get(this.random.nextInt(selectedDiscs.size()));
    }

    @Override
    public Optional<MusicSession> getMusicState(UUID playerUuid) {
        return Optional.ofNullable(activeSessions.get(playerUuid));
    }

    @Override
    public List<EnumMusicDisc> getMusicDiscs() {
        return Arrays.asList(this.allDiscs);
    }

    MusicSession getSession(UUID playerUuid) {
        return activeSessions.get(playerUuid);
    }

    public void sendPlaySoundPacket(Player player, EnumMusicDisc disc, Location location) {
        BlockPosition pos = new BlockPosition(location.getX(), location.getY(), location.getZ());
        PacketPlayOutWorldEvent packet = new PacketPlayOutWorldEvent(RECORD_PLAY_EFFECT_ID, pos, disc.getMaterial().getId(), false);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public void sendStopSoundPacket(Player player, Location location) {
        BlockPosition pos = new BlockPosition(location.getX(), location.getY(), location.getZ());
        PacketPlayOutWorldEvent packet = new PacketPlayOutWorldEvent(RECORD_PLAY_EFFECT_ID, pos, 0, false);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}