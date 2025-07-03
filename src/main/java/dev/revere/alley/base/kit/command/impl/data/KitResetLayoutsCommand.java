package dev.revere.alley.base.kit.command.impl.data;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.kit.IKitService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.kit.KitService;
import dev.revere.alley.config.locale.impl.KitLocale;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Alley
 * @since 30/06/2025
 */
public class KitResetLayoutsCommand extends BaseCommand {
    @CommandData(name = "kit.resetlayouts", isAdminOnly = true, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&6Usage: &e/kit resetlayouts &b<kitName>"));
            return;
        }

        String kitName = args[0];
        IKitService kitService = Alley.getInstance().getService(IKitService.class);
        Kit kit = kitService.getKit(kitName);
        if (kit == null) {
            sender.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        Alley.getInstance().getService(IProfileService.class).resetLayoutForKit(kit);
    }
}