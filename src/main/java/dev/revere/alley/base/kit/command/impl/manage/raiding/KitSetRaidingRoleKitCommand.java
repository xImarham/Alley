package dev.revere.alley.base.kit.command.impl.manage.raiding;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.kit.IKitService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.kit.KitService;
import dev.revere.alley.base.kit.data.BaseRaidingKitData;
import dev.revere.alley.base.kit.service.BaseRaidingService;
import dev.revere.alley.base.kit.service.IBaseRaidingService;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingRaidingImpl;
import dev.revere.alley.game.match.player.enums.EnumBaseRaiderRole;
import dev.revere.alley.util.chat.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Alley
 * @since 16/06/2025
 */
public class KitSetRaidingRoleKitCommand extends BaseCommand {
    @CommandData(name = "kit.setraidingrolekit", isAdminOnly = true, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 3) {
            sender.sendMessage(CC.translate("&6Usage: &e/kit setraidingrolekit &6<kitName> <role> <kitName>"));
            return;
        }

        String kitName = args[0];
        IKitService kitService = Alley.getInstance().getService(IKitService.class);
        Kit kit = kitService.getKit(kitName);
        if (kit == null) {
            sender.sendMessage(CC.translate("&cThe &6" + kitName + " &ckit does not exist."));
            return;
        }

        if (!kit.isSettingEnabled(KitSettingRaidingImpl.class)) {
            sender.sendMessage(CC.translate("&cThe &6" + kit.getName() + " &ckit does not have &6base raiding &csetting enabled."));
            return;
        }

        String roleName = args[1].toUpperCase();
        EnumBaseRaiderRole role;
        try {
            role = EnumBaseRaiderRole.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(CC.translate("&cInvalid raiding role: " + roleName + ". Valid roles are: RAIDER, TRAPPER"));
            return;
        }

        String roleKitName = args[2];
        Kit roleKit = kitService.getKit(roleKitName);
        if (roleKit == null) {
            sender.sendMessage(CC.translate("&cThe &6" + roleKitName + " &ckit does not exist."));
            return;
        }

        if (roleKit.isEnabled()) {
            sender.sendMessage(CC.translate("&cThe &6" + roleKitName + " &ckit is currently enabled. Please disable it before setting it as a raiding role kit."));
            return;
        }

        IBaseRaidingService raidingService = Alley.getInstance().getService(IBaseRaidingService.class);
        raidingService.setRaidingKitMapping(kit, role, roleKit);

        sender.sendMessage(CC.translate("&aSuccessfully set the &6" + role + " &araiding role kit to &6" + roleKit.getName() + "&a for the &6" + kit.getName() + " &akit."));
    }
}