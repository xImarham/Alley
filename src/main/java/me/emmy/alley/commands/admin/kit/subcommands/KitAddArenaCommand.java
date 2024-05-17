/*package me.emmy.alley.commands.admin.kit.subcommands;

import me.emmy.alley.Alley;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

import java.util.List;


public class KitAddArenaCommand extends BaseCommand {
    @Override
    @Command(name = "kit.addarena", permission = "practice.owner")
    public void onCommand(CommandArgs args) {
        Player sender = args.getPlayer();

        if (sender == null)
            return;

        if (args.length() < 2) {
            sender.sendMessage(CC.translate("&cUsage: /kit addarena (kitName) (arenaName)"));
            return;
        }

        String kitName = args.getArgs(0);
        String arenaName = args.getArgs(1);

        Kit kit = Alley.getInstance().getKitManager().getKit(kitName);

        if (kit == null) {
            sender.sendMessage(CC.translate("&cThere is no kit with that name."));
            return;
        }

        Arena arena = Alley.getInstance().getArenaManager().getArenas().stream().filter(p_arena -> p_arena.getName().equalsIgnoreCase(arenaName)).findFirst().orElse(null);
        if (arena == null) {
            sender.sendMessage(CC.translate("&cThere is no arena with that name."));
            return;
        }

        List<String> arenas = kit.getArenas();
        arenas.add(arenaName);

        kit.setArenas(arenas);
        Alley.getInstance().getKitManager().saveKit(kitName, kit);
        sender.sendMessage(CC.translate("&aSuccessfully added the &b" + arenaName + " &aarena to the&b" + kitName + " &akit!"));
    }
}
*/