//package dev.revere.alley.essential.parkour.listener;
//
//import dev.revere.alley.Alley;
//import dev.revere.alley.essential.parkour.data.ParkourData;
//import dev.revere.alley.profile.enums.EnumProfileState;
//import dev.revere.alley.reflection.impl.TitleReflectionService;
//import dev.revere.alley.util.ListenerUtil;
//import dev.revere.alley.util.SoundUtil;
//import dev.revere.alley.util.chat.Symbol;
//import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.Sound;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.block.Action;
//import org.bukkit.event.player.PlayerInteractEvent;
//import org.bukkit.event.player.PlayerMoveEvent;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.concurrent.ThreadLocalRandom;
//
/// **
// * @author Emmy
// * @project Alley
// * @since 25/04/2025
// */
//public class ParkourListener implements Listener {
//    protected final Alley plugin;
//
//    /**
//     * Constructor for the ParkourListener class.
//     *
//     * @param plugin The Alley plugin instance.
//     */
//    public ParkourListener(Alley plugin) {
//        this.plugin = plugin;
//    }
//
//    @EventHandler
//    private void onPlayerMove(PlayerMoveEvent event) {
//        Player player = event.getPlayer();
//
//        if (this.plugin.getProfileService().getProfile(player.getUniqueId()).getState() != EnumProfileState.WAITING) {
//            return;
//        }
//
//        ParkourData parkourData = this.plugin.getParkourService().getParkourData(player);
//        if (parkourData == null) {
//            return;
//        }
//
//        Location location = player.getLocation();
//
//        if (location.getY() < 0 || location.getBlock().getType() == Material.STATIONARY_WATER) {
//            player.teleport(parkourData.getLastCheckpoint());
//
//            SoundUtil.playCustomSound(player, Sound.VILLAGER_DEATH, 1.0f, 1.0f);
//
//            Alley.getInstance().getReflectionRepository().getReflectionService(TitleReflectionService.class).sendTitle(
//                    player,
//                    "&7&l" + Symbol.SKULL,
//                    "&c" + this.getRandomSaltyMessage(),
//                    2, 10, 2
//            );
//        }
//    }
//
//    /**
//     * Returns a random salty message from the list of salty messages.
//     *
//     * @return A random salty message.
//     */
//    private String getRandomSaltyMessage() {
//        List<String> saltyMessages = this.saltyMessages();
//        return saltyMessages.get(ThreadLocalRandom.current().nextInt(0, saltyMessages.size()));
//    }
//
//    /**
//     * Returns a list of salty expression strings.
//     *
//     * @return A list of salty expressions.
//     */
//    private List<String> saltyMessages() {
//        return Arrays.asList(
//                "WHOOPSIES!",
//                "LOL!",
//                "COMMON SENSE!",
//                "HELL NAW!",
//                "aw come on...",
//                "LMAO!",
//                "YIKES!",
//                "Emmy is better.",
//                "WTF!",
//                "NO WAY!",
//                "Bad.",
//                "ARE YOU SERIOUS?",
//                "OH NO!",
//                "DAMN!",
//                "BRO? HOW?",
//                "WHAT THE HELL?",
//                "LOSER!",
//                "HAHA!"
//        );
//    }
//
//    @EventHandler
//    private void onPressurePlateClick(PlayerInteractEvent event) {
//        Player player = event.getPlayer();
//
//        if (this.plugin.getProfileService().getProfile(player.getUniqueId()).getState() != EnumProfileState.WAITING) {
//            return;
//        }
//
//        if (!this.plugin.getParkourService().isPlaying(player)) {
//            return;
//        }
//
//        if (event.getAction() != Action.PHYSICAL) {
//            return;
//        }
//
//        if (ListenerUtil.notSteppingOnPlate(event)) return;
//
//        ParkourData parkourData = this.plugin.getParkourService().getParkourData(player);
//        if (parkourData == null) {
//            return;
//        }
//
//        Location blockLocation = event.getClickedBlock().getLocation();
//
//        Location centeredLocation = new Location(
//                blockLocation.getWorld(),
//                blockLocation.getBlockX() + 0.5,
//                blockLocation.getY(),
//                blockLocation.getBlockZ() + 0.5,
//                player.getLocation().getYaw(),
//                player.getLocation().getPitch()
//        );
//
//        if (parkourData.hasCheckpoint(centeredLocation)) {
//            return;
//        }
//
//        parkourData.addCheckpoint(centeredLocation);
//
//        SoundUtil.playCustomSound(player, Sound.ANVIL_USE, 1.0f, 1.0f);
//
//        this.plugin.getReflectionRepository().getReflectionService(TitleReflectionService.class).sendTitle(
//                player,
//                "&aCHECKPOINT!",
//                "&7(" + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ() + ")",
//                2, 10, 2
//        );
//    }
//}