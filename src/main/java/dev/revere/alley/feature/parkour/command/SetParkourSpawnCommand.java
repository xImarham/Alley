//package dev.revere.alley.feature.parkour.command;
//
//import dev.revere.alley.Alley;
//import dev.revere.alley.api.command.BaseCommand;
//import dev.revere.alley.api.command.CommandArgs;
//import dev.revere.alley.api.command.annotation.CommandData;
//import dev.revere.alley.util.chat.CC;
//import net.citizensnpcs.api.command.Command;
//import org.bukkit.entity.Player;
//
/// **
// * @author Emmy
// * @project Alley
// * @since 26/04/2025
// */
//public class SetParkourSpawnCommand extends BaseCommand {
//    @CommandData(name = "setparkourspawn", isAdminOnly = true)
//    @Override
//    public void onCommand(CommandArgs command) {
//        Player player = command.getPlayer();
//
//        Alley.getInstance().getParkourService().setStarterLocation(player.getLocation());
//        player.sendMessage(CC.translate("&bSuccessfully set the parkour starter location."));
//    }
//}
