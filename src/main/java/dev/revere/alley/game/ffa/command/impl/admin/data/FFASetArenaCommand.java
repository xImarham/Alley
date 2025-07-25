package dev.revere.alley.game.ffa.command.impl.admin.data;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.arena.IArenaService;
import dev.revere.alley.base.arena.enums.EnumArenaType;
import dev.revere.alley.base.kit.IKitService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.config.locale.impl.ArenaLocale;
import dev.revere.alley.game.ffa.IFFAService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project alley-practice
 * @since 25/07/2025
 */
public class FFASetArenaCommand extends BaseCommand {
    @CommandData(
            name = "ffa.setarena",
            isAdminOnly = true,
            inGameOnly = false
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&6Usage: &e/ffa setarena &6<arenaName>"));
            return;
        }

        String arenaName = args[0];
        IArenaService arenaService = this.plugin.getService(IArenaService.class);
        AbstractArena arena = arenaService.getArenaByName(arenaName);
        if (arena == null) {
            sender.sendMessage(ArenaLocale.NOT_FOUND.getMessage().replace("{arena-name}", arenaName));
            return;
        }

        if (arena.getType() != EnumArenaType.FFA) {
            sender.sendMessage(CC.translate("&cYou can only set the arena for Free-For-All arenas!"));
            return;
        }

        IKitService kitService = this.plugin.getService(IKitService.class);
        Kit kit = kitService.getKit(arenaName);
        if (kit == null) {
            sender.sendMessage(CC.translate("&cA kit with that name does not exist!"));
            return;
        }

        if (!kit.isFfaEnabled()) {
            sender.sendMessage(CC.translate("&cFFA mode is not enabled for this kit!"));
            return;
        }

        kit.setFfaArenaName(arena.getName());
        kitService.saveKit(kit);
        this.plugin.getService(IFFAService.class).reloadFFAKits();
        sender.sendMessage(CC.translate("&aFFA arena has been set for kit &6" + kit.getName() + "&a!"));
    }
}